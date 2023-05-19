package capstone.miso.dishcovery.application.service;

import capstone.miso.dishcovery.domain.keyword.KeywordSet;
import capstone.miso.dishcovery.domain.keyword.repository.KeywordDataRepository;
import capstone.miso.dishcovery.domain.keyword.repository.KeywordRepository;
import capstone.miso.dishcovery.domain.store.dto.StoreSearchCondition;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreAndKeywordService {
    private final StoreRepository storeRepository;
    private final KeywordDataRepository keywordDataRepository;
    private final KeywordRepository keywordRepository;
    public List<StoreShortDTO> findStoreByKeywordRank(KeywordSet keywordSet, int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);

        List<Long> storeIds = switch (keywordSet) {
            // KeywordData Table
            case BREAKFAST -> keywordDataRepository.findStoreOrderByBreakfast(keywordSet, pageRequest);
            case LUNCH -> keywordDataRepository.findStoreOrderByLunch(keywordSet, pageRequest);
            case DINNER -> keywordDataRepository.findStoreOrderByDinner(keywordSet, pageRequest);
            case SPRING -> keywordDataRepository.findStoreOrderBySpring(keywordSet, pageRequest);
            case SUMMER -> keywordDataRepository.findStoreOrderBySummer(keywordSet, pageRequest);
            case FALL -> keywordDataRepository.findStoreOrderByFall(keywordSet, pageRequest);
            case WINTER -> keywordDataRepository.findStoreOrderByWinter(keywordSet, pageRequest);
            case UNDER_COST_8000 -> keywordDataRepository.findStoreOrderByCostUnder8000(keywordSet, pageRequest);
            case UNDER_COST_15000 -> keywordDataRepository.findStoreOrderByCostUnder15000(keywordSet, pageRequest);
            case UNDER_COST_25000 -> keywordDataRepository.findStoreOrderByCostUnder25000(keywordSet, pageRequest);
            case OVER_COST_25000 -> keywordDataRepository.findStoreOrderByCostOver25000(keywordSet, pageRequest);
            case UNDER_PARTICIPANTS_5 -> keywordDataRepository.findStoreOrderBySmallGroup(keywordSet, pageRequest);
            case UNDER_PARTICIPANTS_10 -> keywordDataRepository.findStoreOrderByMediumGroup(keywordSet, pageRequest);
            case UNDER_PARTICIPANTS_20 -> keywordDataRepository.findStoreOrderByLargeGroup(keywordSet, pageRequest);
            case OVER_PARTICIPANTS_20 -> keywordDataRepository.findStoreOrderByExtraGroup(keywordSet, pageRequest);
            // Keyword (Store Keyword) Table
            case FOUR_SEASONS -> keywordRepository.findStoreIdByKeyword(keywordSet, pageRequest);
            default -> throw new IllegalArgumentException("Not supported keyword");
        };

        StoreSearchCondition condition = new StoreSearchCondition(storeIds);
        Page<StoreShortDTO> storeShortDTOS = storeRepository.searchAllStoreShort(condition, null);
        return storeShortDTOS.getContent();
    }
}
