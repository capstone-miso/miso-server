package capstone.miso.dishcovery.domain.store.service;

import capstone.miso.dishcovery.application.files.dto.KakaoStoreDetailDTO;
import capstone.miso.dishcovery.application.files.mapping.KakaoStoreDetailExtractor;
import capstone.miso.dishcovery.application.files.repository.FileDataJdbcRepository;
import capstone.miso.dishcovery.domain.keyword.Keyword;
import capstone.miso.dishcovery.domain.keyword.KeywordData;
import capstone.miso.dishcovery.domain.keyword.repository.KeywordDataRepository;
import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.parkinglot.dto.ParkingDTO;
import capstone.miso.dishcovery.domain.parkinglot.repository.ParkingJDBCRepository;
import capstone.miso.dishcovery.domain.preference.repository.PreferenceRepository;
import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.dto.StoreDetailDTO;
import capstone.miso.dishcovery.domain.store.dto.StoreKeywordDataDTO;
import capstone.miso.dishcovery.domain.store.dto.StoreSearchCondition;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.store.repository.StoreJDBCRepository;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import capstone.miso.dishcovery.dto.PageRequestDTO;
import capstone.miso.dishcovery.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/
@Log4j2
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;
    private final StoreJDBCRepository storeJDBCRepository;
    private final FileDataJdbcRepository fileDataJdbcRepository;
    private final KeywordDataRepository keywordDataRepository;
    private final PreferenceRepository preferenceRepository;
    private final KakaoStoreDetailExtractor kakaoStoreDetailExtractor;
    private final ParkingJDBCRepository parkingJDBCRepository;
    private final ModelMapper modelMapper;

    @Override
    public PageResponseDTO<StoreShortDTO> listWithStoreShort(PageRequestDTO pageRequestDTO, Member member) {
        StoreSearchCondition condition = pageRequestDTO.getStoreSearchCondition();
        condition.setStoreId(pageRequestDTO.getStoreId());
        condition.setMember(member);

        Pageable pageable = pageRequestDTO.getPageable();

        Page<StoreShortDTO> result = storeRepository.searchAllStoreShort(condition, pageable);

        return PageResponseDTO.<StoreShortDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    @Transactional
    public StoreDetailDTO getStoreDetail(Long sid, Member member) {
        Optional<Store> result = storeRepository.findById(sid);
        Store store = result.orElseThrow(RuntimeException::new);
        StoreDetailDTO storeDetailDTO = StoreDetailDTO.builder()
                .id(store.getSid())
                .storeName(store.getName())
                .lat(store.getLat())
                .lon(store.getLon())
                .mainImageUrl(store.getMainImageUrl())
                .phone(store.getPhone())
                .address(store.getAddress())
                .category(store.getCategory())
                .sector(store.getSector())
                .build();

        // 매장 키워드 조회
        List<Keyword> storeKeywords = store.getKeywords();
        List<String> keywords = storeKeywords.stream().map(keyword -> keyword.getKeyword().getKorean()).toList();
        storeDetailDTO.setKeywords(keywords);
        storeDetailDTO.setVisitedTime(fileDataJdbcRepository.getStoreTimeTableDTO(sid));

        // 관심 매장 개수
        storeDetailDTO.setPreferenceCount(preferenceRepository.countByStoreId(sid));

        // Keyword Data 추가 (방문 횟수, 총 이용 금액 등 키워드 데이터)
        Optional<KeywordData> storeKeywordData = keywordDataRepository.findTopByStore(store);
        if (storeKeywordData.isPresent()) {
            StoreKeywordDataDTO storeKeywordDataDTO = modelMapper.map(storeKeywordData.get(), StoreKeywordDataDTO.class);
            storeKeywordDataDTO.initTotal();
            storeDetailDTO.setKeywordData(storeKeywordDataDTO);
        }
        // storeDetail preference setting
        if (member != null) {
            boolean checkMyStorePreference = preferenceRepository.checkMyStorePreference(member, sid);
            storeDetailDTO.setPreference(checkMyStorePreference);
        }

        // 매장의 카카오 정보 추가
        KakaoStoreDetailDTO kakaoStoreDetailDTO = new KakaoStoreDetailDTO();
        try {
            kakaoStoreDetailDTO = kakaoStoreDetailExtractor.getKakaoStoreDetailDTO(sid);
            storeDetailDTO.setStoreInfo(kakaoStoreDetailDTO);
        } catch (IOException ignored) {
        }

        // 공영 주차장 정보
        try {
            ParkingDTO parkinglot = parkingJDBCRepository.findCloseParkinglot(
                    storeDetailDTO.getLon(), storeDetailDTO.getLat()
            );
            kakaoStoreDetailDTO.getFindway().setParkingZone(parkinglot);
        } catch (Exception ignored) {
        }
        return storeDetailDTO;
    }

    @Override
    public List<StoreShortDTO> getSimilarStoreShorts(Long sid, Member member) {
        Optional<Store> result = storeRepository.findById(sid);
        Store store = result.orElseThrow(RuntimeException::new);

        // storeShort 데이터 추가
        // 추천 매장 데이터 출력
        String category = store.getCategory();
        String[] categorySegments = category.split(">");
        List<Long> storeIds = null;
        if (categorySegments.length >= 2) {
            storeIds = storeJDBCRepository.findSimilarWithNowStore(sid, categorySegments[1].trim());
        } else if (categorySegments.length == 1) {
            storeIds = storeJDBCRepository.findSimilarWithNowStore(sid, categorySegments[0].trim());
        }
        // 해당 매장의 키워드 정보가 없는 경우 카테고리 만으로 비슷한 매장 찾기
        if (storeIds == null || storeIds.size() == 0) {
            storeIds = storeJDBCRepository.findSimilarWithNowStoreOnlyCategory(category.trim());
        }
        // 그래도 없는 경우?
        if (storeIds == null) {
            return new ArrayList<>();
        }
        storeIds.remove(sid);
        StoreSearchCondition condition = new StoreSearchCondition();
        condition.setPreference(0L);
        condition.setStoreIds(storeIds);
        // 또갈집에 등록되지 않는 매장을 가져오도록 함
        if (member != null) {
            condition.setMember(member);
            condition.setPreference(2L);
        }
        Page<StoreShortDTO> storeShortDTOS = storeRepository.searchAllStoreShort(condition, null);
        return storeShortDTOS.getContent();
    }
}
