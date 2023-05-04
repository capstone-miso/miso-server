package capstone.miso.dishcovery.application.service;

import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.preference.Preference;
import capstone.miso.dishcovery.domain.preference.dto.DeletePreferenceRes;
import capstone.miso.dishcovery.domain.preference.dto.SavePreferenceRes;
import capstone.miso.dishcovery.domain.preference.repository.PreferenceRepository;
import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.dto.StoreDetailDTO;
import capstone.miso.dishcovery.domain.store.dto.StoreSearchCondition;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import capstone.miso.dishcovery.domain.store.service.StoreService;
import capstone.miso.dishcovery.domain.store.service.StoreServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final StoreRepository storeRepository;
    private final PreferenceRepository preferenceRepository;
    private final ModelMapper modelMapper;
    private final StoreService storeService;

    public SavePreferenceRes savePreference(Member member, Long storeId) {
        Optional<Store> findStore = storeRepository.findById(storeId);
        Store store = findStore.orElseThrow(() -> new IllegalArgumentException("Invalid store id: " + storeId));

        Preference preference = Preference.builder()
                .member(member)
                .store(store)
                .build();

        preferenceRepository.save(preference);
        return new SavePreferenceRes("Save store in preference");
    }

    public DeletePreferenceRes deletePreference(Member member, Long storeId) {
        Optional<Store> findStore = storeRepository.findById(storeId);
        Store store = findStore.orElseThrow(() -> new IllegalArgumentException("Invalid store id: " + storeId));

        Optional<Preference> result = preferenceRepository.findByMemberAndStore(member, store);
        Preference preference = result.orElseThrow(() -> new RuntimeException("또갈집을 찾지 못했어요"));

        preferenceRepository.delete(preference);
        return new DeletePreferenceRes("Delete store from preference");
    }

    public List<StoreShortDTO> findMyStores(Member member) {
        List<StoreShortDTO> stores = new ArrayList<>();

        List<Preference> preferences = preferenceRepository.findByMemberOrderByUpdatedAtDesc(member);
        preferences.forEach(preference -> {
            Long storeId = preferenceRepository.findStoreIdByPreferenceKey(preference.getPid());
            Page<StoreShortDTO> storeShortDTOS = storeRepository.searchAllStoreShort(new StoreSearchCondition(storeId), null);
            stores.addAll(storeShortDTOS.getContent());
        });
        return stores;
    }

    public List<StoreShortDTO> famousStore(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        var result = preferenceRepository.findFamousStores(pageRequest);
        List<StoreShortDTO> famousStores=new ArrayList<>();
        for (Long sid : result) {
            StoreDetailDTO store =storeService.getStoreDetail(sid);
            StoreShortDTO shortDTO = modelMapper.map(store, StoreShortDTO.class);
            shortDTO.setStoreName(store.getName());
            famousStores.add(shortDTO);
        }
        return famousStores;
    }
}
