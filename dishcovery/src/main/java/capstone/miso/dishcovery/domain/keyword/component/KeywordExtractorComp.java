package capstone.miso.dishcovery.domain.keyword.component;

import capstone.miso.dishcovery.domain.keyword.Keyword;
import capstone.miso.dishcovery.domain.keyword.KeywordData;
import capstone.miso.dishcovery.domain.keyword.KeywordSet;
import capstone.miso.dishcovery.domain.keyword.repository.KeywordDataRepository;
import capstone.miso.dishcovery.domain.keyword.repository.KeywordRepository;
import capstone.miso.dishcovery.domain.store.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class KeywordExtractorComp {
    public final KeywordRepository keywordRepository;
    private final KeywordDataRepository keywordDataRepository;

    private void insertKeyword(Store store, KeywordSet keywordSet) {
        Keyword keyword = Keyword.builder()
                .store(store)
                .keyword(keywordSet)
                .build();

        keywordRepository.save(keyword);
    }

    public void keywordExtract() {
        List<KeywordData> allKeywordData = keywordDataRepository.findAll();
        log.info("Selected Keyword Total Count: " + allKeywordData.size());
        int count = 0;
        for (KeywordData keywordData : allKeywordData) {
            if (++count % 1000 == 0){
                log.info("Keyword extract... count: " + count + " StoreId: " + keywordData.getStoreId());
            }
            if (keywordData.getTotalVisited() < 5)
                continue;
            Store store = keywordData.getStore();

            // 시간대
            extractedByTime(keywordData, store);
            // 계절
            extractedBySeason(keywordData, store);
            // 인원수
            extractedByParticipant(keywordData, store);
            // 가격대
            extractedByCost(keywordData, store);
        }
        extractNotableKeyword(allKeywordData);
    }

    private void extractNotableKeyword(List<KeywordData> allKeywordData) {
        PageRequest pageRequest = PageRequest.of(0, allKeywordData.size() / 10);
        List<KeywordData> visitedDesc = keywordDataRepository.findByOrderByTotalVisitedDesc(pageRequest);
        List<KeywordData> costDesc = keywordDataRepository.findByOrderByTotalCostDesc(pageRequest);
        List<KeywordData> participantsDesc = keywordDataRepository.findByOrderByTotalParticipantsDesc(pageRequest);
        for (KeywordData data : visitedDesc) {
            insertKeyword(data.getStore(), KeywordSet.HIGH_TOTAL_VISITED);
        }
        for (KeywordData data : costDesc) {
            insertKeyword(data.getStore(), KeywordSet.HIGH_TOTAL_COST);
        }
        for (KeywordData data : participantsDesc) {
            insertKeyword(data.getStore(), KeywordSet.HIGH_TOTAL_PARTICIPANTS);
        }
    }

    private void extractedByCost(KeywordData keywordData, Store store) {
        long costUnder8000 = keywordData.getCostUnder8000();
        long costUnder15000 = keywordData.getCostUnder15000();
        long costUnder25000 = keywordData.getCostUnder25000();
        long costOver25000 = keywordData.getCostOver25000();
        double totalCost = costUnder8000 + costUnder15000 + costUnder15000 + costOver25000;
        if (costUnder8000 / totalCost >= 0.4) {
            insertKeyword(store, KeywordSet.UNDER_COST_8000);
        }
        if (costUnder15000 / totalCost >= 0.4) {
            insertKeyword(store, KeywordSet.UNDER_COST_15000);
        }
        if (costUnder25000 / totalCost >= 0.4) {
            insertKeyword(store, KeywordSet.UNDER_COST_25000);
        }
        if (costOver25000 / totalCost >= 0.4) {
            insertKeyword(store, KeywordSet.OVER_COST_25000);
        }
    }

    private void extractedByParticipant(KeywordData keywordData, Store store) {
        long smallGroup = keywordData.getSmallGroup();
        long mediumGroup = keywordData.getMediumGroup();
        long largeGroup = keywordData.getLargeGroup();
        long extraGroup = keywordData.getExtraGroup();
        double totalParticipants = smallGroup + mediumGroup + largeGroup + extraGroup;
        if (smallGroup / totalParticipants >= 0.4) {
            insertKeyword(store, KeywordSet.UNDER_PARTICIPANTS_5);
        }
        if (mediumGroup / totalParticipants >= 0.4) {
            insertKeyword(store, KeywordSet.UNDER_PARTICIPANTS_10);
        }
        if (largeGroup / totalParticipants >= 0.4) {
            insertKeyword(store, KeywordSet.UNDER_PARTICIPANTS_20);
        }
        if (extraGroup / totalParticipants >= 0.4) {
            insertKeyword(store, KeywordSet.OVER_PARTICIPANTS_20);
        }
    }

    private void extractedByTime(KeywordData keywordData, Store store) {
        long breakfast = keywordData.getBreakfast();
        long lunch = keywordData.getLunch();
        long dinner = keywordData.getDinner();
        long totalTimes = breakfast + lunch + dinner;
        if (breakfast / (double) totalTimes >= 0.4) {
            insertKeyword(store, KeywordSet.BREAKFAST);
        }
        if (lunch / (double) totalTimes >= 0.4) {
            insertKeyword(store, KeywordSet.LUNCH);
        }
        if (dinner / (double) totalTimes >= 0.4) {
            insertKeyword(store, KeywordSet.DINNER);
        }
    }

    private void extractedBySeason(KeywordData keywordData, Store store) {
        long spring = keywordData.getSpring();
        long summer = keywordData.getSummer();
        long fall = keywordData.getFall();
        long winter = keywordData.getWinter();
        double totalSeasons = spring + summer + fall + winter;
        if (spring / totalSeasons >= 0.4) {
            insertKeyword(store, KeywordSet.SPRING);
        }
        if (summer / totalSeasons >= 0.4) {
            insertKeyword(store, KeywordSet.SUMMER);
        }
        if (fall / totalSeasons >= 0.4) {
            insertKeyword(store, KeywordSet.FALL);
        }
        if (winter / totalSeasons >= 0.4) {
            insertKeyword(store, KeywordSet.WINTER);
        }
        if (checkFourSeason(spring, summer, fall, winter, totalSeasons)) {
            insertKeyword(store, KeywordSet.FOUR_SEASONS);
        }
    }

    private boolean checkFourSeason(long spring, long summer, long fall, long winter, double totalSeasons) {
        int cnt = 0;
        if (spring / totalSeasons >= 0.2) {
            cnt += 1;
        }
        if (summer / totalSeasons >= 0.2) {
            cnt += 1;
        }
        if (fall / totalSeasons >= 0.2) {
            cnt += 1;
        }
        if (winter / totalSeasons >= 0.2) {
            cnt += 1;
        }
        return cnt >= 3;
    }
}
