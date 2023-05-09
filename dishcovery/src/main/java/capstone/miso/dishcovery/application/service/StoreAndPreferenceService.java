package capstone.miso.dishcovery.application.service;

import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.preference.Preference;
import capstone.miso.dishcovery.domain.preference.repository.PreferenceRepository;
import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.dto.StoreDetailDTO;
import capstone.miso.dishcovery.domain.store.dto.StoreSearchCondition;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import capstone.miso.dishcovery.domain.store.service.StoreService;
import capstone.miso.dishcovery.dto.PageRequestDTO;
import capstone.miso.dishcovery.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final StoreService storeService;
    private final StoreRepository storeRepository;
    private final PreferenceRepository preferenceRepository;
    private final ModelMapper modelMapper;

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

    public List<StoreShortDTO> findMyStores(Member member, int page, int size) {
        List<StoreShortDTO> stores = new ArrayList<>();

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Preference> preferences = preferenceRepository.findByMemberOrderByUpdatedAtDesc(member, pageRequest);
        preferences.getContent().forEach(preference -> {
            Long storeId = preferenceRepository.findStoreIdByPreferenceKey(preference.getPid());
            Page<StoreShortDTO> storeShortDTOS = storeRepository.searchAllStoreShort(new StoreSearchCondition(storeId), null);
            storeShortDTOS.getContent().forEach(storeShortDTO -> storeShortDTO.setPreference(true));

            stores.addAll(storeShortDTOS.getContent());
        });
        return stores;
    }

    public List<StoreShortDTO> famousStore(int page, int size, Member member) {
        PageRequest pageRequest = PageRequest.of(page, size);
        var result = preferenceRepository.findFamousStores(pageRequest);

        List<StoreShortDTO> famousStores = new ArrayList<>();
        for (Long sid : result) {
            StoreDetailDTO store = storeService.getStoreDetail(sid);
            StoreShortDTO storeShortDTO = modelMapper.map(store, StoreShortDTO.class);
            storeShortDTO.setPreference(checkMyStorePreference(member, sid));

            if (storeShortDTO.isPreference())
                continue;
            famousStores.add(storeShortDTO);
        }
        return famousStores;
    }

    public PageResponseDTO<StoreShortDTO> listWithStoreShortWithPreference(PageRequestDTO pageRequestDTO, Member member) {
        PageResponseDTO<StoreShortDTO> result = storeService.listWithStoreShort(pageRequestDTO);
        result.getDtoList().forEach(storeShortDTO -> storeShortDTO.setPreference(checkMyStorePreference(member, storeShortDTO.getId())));
        return result;
    }

    public StoreDetailDTO getStoreDetailWithPreference(Long sid, Member member) {
        StoreDetailDTO result = storeService.getStoreDetail(sid);
        result.setPreference(checkMyStorePreference(member, sid));
        return result;
    }

    private boolean checkMyStorePreference(Member member, Long storeId) {
        List<Long> result = preferenceRepository.checkMyStorePreference(member, storeId, PageRequest.of(0, 1));
        if (result.size() > 0)
            return true;
        return false;
    }
}
