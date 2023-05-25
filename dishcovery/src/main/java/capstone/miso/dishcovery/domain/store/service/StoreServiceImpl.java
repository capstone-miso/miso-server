package capstone.miso.dishcovery.domain.store.service;

import capstone.miso.dishcovery.application.files.repository.FileDataJdbcRepository;
import capstone.miso.dishcovery.domain.image.Image;
import capstone.miso.dishcovery.domain.keyword.Keyword;
import capstone.miso.dishcovery.domain.keyword.KeywordData;
import capstone.miso.dishcovery.domain.keyword.KeywordSet;
import capstone.miso.dishcovery.domain.keyword.dao.KeywordGroupDTO;
import capstone.miso.dishcovery.domain.keyword.repository.KeywordDataRepository;
import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.menu.dto.MenuDTO;
import capstone.miso.dishcovery.domain.preference.repository.PreferenceRepository;
import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.StoreOffInfo;
import capstone.miso.dishcovery.domain.store.StoreOnInfo;
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
                .phone(store.getPhone())
                .address(store.getAddress())
                .category(store.getCategory())
                .sector(store.getSector())
                .build();

        List<String> onInfo = store.getStoreOnInfos().stream().map(StoreOnInfo::getInfo).toList();
        List<String> offInfo = store.getStoreOffInfos().stream().map(StoreOffInfo::getInfo).toList();
        List<MenuDTO> menus = store.getMenus().stream().map(menu -> new MenuDTO(menu.getMid(), menu.getName(), menu.getCost(), menu.getCost(), menu.getMenuImg())).toList();
        List<String> images = store.getImages().stream().map(Image::getImageUrl).toList();
        String mainImage = store.getImages().stream().filter(image1 -> image1.getPhotoId().equals("M")).findFirst().orElse(
                store.getImages().stream().findFirst().orElse(new Image())
        ).getImageUrl();
        // 매장 키워드 조회
        List<Keyword> storeKeywords = store.getKeywords();
        List<KeywordSet> keywords = storeKeywords.stream().map(Keyword::getKeyword).toList();

        storeDetailDTO.setOnInfo(onInfo);
        storeDetailDTO.setOffInfo(offInfo);
        storeDetailDTO.setMenus(menus);
        storeDetailDTO.setMainImage(mainImage);
        storeDetailDTO.setImages(images);
        storeDetailDTO.setKeywords(new KeywordGroupDTO(keywords));
        storeDetailDTO.setVisitedTime(fileDataJdbcRepository.getStoreTimeTableDTO(sid));

        // Keyword Data 추가
        Optional<KeywordData> storeKeywordData = keywordDataRepository.findTopByStore(store);
        if (storeKeywordData.isPresent()) {
            StoreKeywordDataDTO storeKeywordDataDTO = modelMapper.map(storeKeywordData.get(), StoreKeywordDataDTO.class);
            storeKeywordDataDTO.init();
            storeDetailDTO.setKeywordData(storeKeywordDataDTO);
        }
        if (member != null) {
            // storeDetail preference setting
            boolean checkMyStorePreference = preferenceRepository.checkMyStorePreference(member, sid);
            storeDetailDTO.setPreference(checkMyStorePreference);
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
        String[] categorySegments = category.split(" > ");
        List<Long> storeIds = null;
        if (categorySegments.length >= 2) {
            storeIds = storeJDBCRepository.findSimilarWithNowStore(sid, "%" + categorySegments[1] + "%");
        } else if (categorySegments.length == 1) {
            storeIds = storeJDBCRepository.findSimilarWithNowStore(sid, "%" + categorySegments[0] + "%");
        }
        if (storeIds == null || storeIds.size() == 0) {
            return new ArrayList<>(); // 비슷한 인기 매장이 없는 경우 빈 리스트 리턴
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
