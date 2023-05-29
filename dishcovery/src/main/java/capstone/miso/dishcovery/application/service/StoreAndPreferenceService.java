package capstone.miso.dishcovery.application.service;

import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.preference.Preference;
import capstone.miso.dishcovery.domain.preference.repository.PreferenceRepository;
import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.dto.StoreDetailDTO;
import capstone.miso.dishcovery.domain.store.dto.StoreSearchCondition;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.store.repository.StoreJDBCRepository;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import capstone.miso.dishcovery.domain.store.service.StoreService;
import capstone.miso.dishcovery.dto.PageRequestDTO;
import capstone.miso.dishcovery.dto.PageResponseDTO;
import capstone.miso.dishcovery.dto.SimplePageRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * author        : duckbill413
 * date          : 2023-05-04
 * description   :
 **/
@Log4j2
@Service
@RequiredArgsConstructor
public class StoreAndPreferenceService {
    private final StoreRepository storeRepository;
    private final PreferenceRepository preferenceRepository;

    public void savePreference(Member member, Long storeId) {
        Optional<Store> findStore = storeRepository.findById(storeId);
        Store store = findStore.orElseThrow(() -> new IllegalArgumentException("Invalid store id: " + storeId));

        Preference preference = Preference.builder()
                .member(member)
                .store(store)
                .build();

        preferenceRepository.save(preference);
    }

    public void deletePreference(Member member, Long storeId) {
        Optional<Store> findStore = storeRepository.findById(storeId);
        Store store = findStore.orElseThrow(() -> new IllegalArgumentException("Invalid store id: " + storeId));

        Optional<Preference> result = preferenceRepository.findByMemberAndStore(member, store);
        Preference preference = result.orElseThrow(() -> new RuntimeException("또갈집을 찾지 못했어요"));

        preferenceRepository.delete(preference);
    }

    public PageResponseDTO<StoreShortDTO> findMyPreferenceStores(Member member, SimplePageRequestDTO pageRequestDTO) {
        StoreSearchCondition condition = pageRequestDTO.getStoreSearchCondition();
        condition.setPreference(1L);
        condition.setMember(member);
        Pageable pageable = pageRequestDTO.getPageable("updatedAt.desc");

        // 나의 관심 매장 조회
        Page<StoreShortDTO> storeShortDTOS = storeRepository.searchAllStoreShort(condition, pageable);
        storeShortDTOS.forEach(storeShortDTO -> storeShortDTO.setPreference(true));

        return PageResponseDTO.<StoreShortDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(storeShortDTOS.getContent())
                .total((int) storeShortDTOS.getTotalElements())
                .build();
    }

    public PageResponseDTO<StoreShortDTO> famousStore(SimplePageRequestDTO pageRequestDTO, Member member) {
        StoreSearchCondition condition = pageRequestDTO.getStoreSearchCondition();
        condition.setPreference(2L);
        condition.setMember(member);
        Pageable pageable = pageRequestDTO.getPageable();
        // 나의 관심 목록에 없는 유명한 매장 조회
        Page<Long> storeIds = preferenceRepository.findFamousStores(member, pageable);
        condition.setStoreIds(storeIds.getContent());
        Page<StoreShortDTO> storeShortDTOS = storeRepository.searchAllStoreShort(condition, PageRequest.of(0, pageable.getPageSize()));
        return PageResponseDTO.<StoreShortDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(storeShortDTOS.getContent())
                .total((int) storeIds.getTotalElements())
                .build();
    }

    public PageResponseDTO<StoreShortDTO> similarStore(SimplePageRequestDTO pageRequestDTO, Member member) {
        StoreSearchCondition condition = pageRequestDTO.getStoreSearchCondition();
        condition.setPreference(2L);
        condition.setMember(member);
        Pageable pageable = pageRequestDTO.getPageable();
        // 나의 관심 매장과 유사한 매장 조회
        Page<Long> storeIds = preferenceRepository.findStoreInMyInterest(member.getEmail(), pageable);
        condition.setStoreIds(storeIds.getContent());

        Page<StoreShortDTO> storeShortDTOS = storeRepository.searchAllStoreShort(condition, PageRequest.of(0, pageable.getPageSize()));
        return PageResponseDTO.<StoreShortDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(storeShortDTOS.getContent())
                .total((int) storeIds.getTotalElements())
                .build();
    }
}
