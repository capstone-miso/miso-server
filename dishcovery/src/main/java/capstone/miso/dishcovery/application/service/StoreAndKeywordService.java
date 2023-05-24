package capstone.miso.dishcovery.application.service;

import capstone.miso.dishcovery.domain.keyword.KeywordSet;
import capstone.miso.dishcovery.domain.keyword.repository.KeywordDataRepository;
import capstone.miso.dishcovery.domain.keyword.repository.KeywordRepository;
import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.store.dto.StoreSearchCondition;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import capstone.miso.dishcovery.dto.PageResponseDTO;
import capstone.miso.dishcovery.dto.SimplePageRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreAndKeywordService {
    private final StoreRepository storeRepository;
    private final KeywordDataRepository keywordDataRepository;
    private final KeywordRepository keywordRepository;

    public PageResponseDTO<StoreShortDTO> findStoreByKeywordRank(KeywordSet keywordSet, SimplePageRequestDTO pageRequestDTO, Member member) {
        Pageable reqPageable = pageRequestDTO.getPageable();
        int page = reqPageable.getPageNumber();
        int size = reqPageable.getPageSize();

        Pageable pageable = PageRequest.of(page, size);

        Page<Long> storeIds = switch (keywordSet) {
            // KeywordData Table
            case BREAKFAST -> keywordDataRepository.findStoreOrderByBreakfast(keywordSet, pageable);
            case LUNCH -> keywordDataRepository.findStoreOrderByLunch(keywordSet, pageable);
            case DINNER -> keywordDataRepository.findStoreOrderByDinner(keywordSet, pageable);
            case SPRING -> keywordDataRepository.findStoreOrderBySpring(keywordSet, pageable);
            case SUMMER -> keywordDataRepository.findStoreOrderBySummer(keywordSet, pageable);
            case FALL -> keywordDataRepository.findStoreOrderByFall(keywordSet, pageable);
            case WINTER -> keywordDataRepository.findStoreOrderByWinter(keywordSet, pageable);
            case UNDER_COST_8000 -> keywordDataRepository.findStoreOrderByCostUnder8000(keywordSet, pageable);
            case UNDER_COST_15000 -> keywordDataRepository.findStoreOrderByCostUnder15000(keywordSet, pageable);
            case UNDER_COST_25000 -> keywordDataRepository.findStoreOrderByCostUnder25000(keywordSet, pageable);
            case OVER_COST_25000 -> keywordDataRepository.findStoreOrderByCostOver25000(keywordSet, pageable);
            case UNDER_PARTICIPANTS_5 -> keywordDataRepository.findStoreOrderBySmallGroup(keywordSet, pageable);
            case UNDER_PARTICIPANTS_10 -> keywordDataRepository.findStoreOrderByMediumGroup(keywordSet, pageable);
            case UNDER_PARTICIPANTS_20 -> keywordDataRepository.findStoreOrderByLargeGroup(keywordSet, pageable);
            case OVER_PARTICIPANTS_20 -> keywordDataRepository.findStoreOrderByExtraGroup(keywordSet, pageable);
            // Keyword (Store Keyword) Table
            case FOUR_SEASONS -> keywordRepository.findStoreIdByKeyword(keywordSet, pageable);
            default -> throw new IllegalArgumentException("Not supported keyword");
        };

        StoreSearchCondition condition = new StoreSearchCondition();
        condition.setStoreIds(storeIds.getContent());
        condition.setMember(member);

        Page<StoreShortDTO> storeShortDTOS = storeRepository.searchAllStoreShort(condition, PageRequest.of(0, size));

        return PageResponseDTO.<StoreShortDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(storeShortDTOS.getContent())
                .total((int) storeIds.getTotalElements())
                .build();
    }
}
