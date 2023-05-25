package capstone.miso.dishcovery.domain.keyword.dto;

import capstone.miso.dishcovery.domain.keyword.KeywordSet;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static capstone.miso.dishcovery.domain.keyword.KeywordSet.*;

@Data
@Builder
@AllArgsConstructor
public class KeywordGroupDTO {
    private KeywordSetGroupEngDTO eng;
    private KeywordSetGroupKorDTO kor;

    public KeywordGroupDTO() {
        List<String> eng_time = KeywordGroupDTOUtil.TIME.stream().map(KeywordSet::name).toList();
        List<String> eng_season = KeywordGroupDTOUtil.SEASON.stream().map(KeywordSet::name).toList();
        List<String> eng_cost = KeywordGroupDTOUtil.COST.stream().map(KeywordSet::name).toList();
        List<String> eng_group = KeywordGroupDTOUtil.GROUP.stream().map(KeywordSet::name).toList();
        List<String> eng_top10 = KeywordGroupDTOUtil.TOP10.stream().map(KeywordSet::name).toList();
        this.eng = new KeywordSetGroupEngDTO(eng_time, eng_season, eng_cost, eng_group, eng_top10);
        List<String> kor_time = KeywordGroupDTOUtil.TIME.stream().map(KeywordSet::getKorean).toList();
        List<String> kor_season = KeywordGroupDTOUtil.SEASON.stream().map(KeywordSet::getKorean).toList();
        List<String> kor_cost = KeywordGroupDTOUtil.COST.stream().map(KeywordSet::getKorean).toList();
        List<String> kor_group = KeywordGroupDTOUtil.GROUP.stream().map(KeywordSet::getKorean).toList();
        List<String> kor_top10 = KeywordGroupDTOUtil.TOP10.stream().map(KeywordSet::getKorean).toList();
        this.kor = new KeywordSetGroupKorDTO(kor_time, kor_season, kor_cost, kor_group, kor_top10);
    }

    private static class KeywordGroupDTOUtil {
        public static final ArrayList<KeywordSet> TIME = new ArrayList<>(Arrays.asList(BREAKFAST, LUNCH, DINNER));
        public static final ArrayList<KeywordSet> SEASON = new ArrayList<>(Arrays.asList(SPRING, SUMMER, FALL, WINTER, FOUR_SEASONS));
        public static final ArrayList<KeywordSet> COST = new ArrayList<>(Arrays.asList(UNDER_COST_8000, UNDER_COST_15000, UNDER_COST_25000, OVER_COST_25000));
        public static final ArrayList<KeywordSet> GROUP = new ArrayList<>(Arrays.asList(UNDER_PARTICIPANTS_5, UNDER_PARTICIPANTS_10, UNDER_PARTICIPANTS_20, OVER_PARTICIPANTS_20));
        public static final ArrayList<KeywordSet> TOP10 = new ArrayList<>(Arrays.asList(HIGH_TOTAL_COST, HIGH_TOTAL_PARTICIPANTS, HIGH_TOTAL_VISITED));

        public static void setKeywordSet(Set<KeywordSet> keywords, List<String> time, List<String> season, List<String> cost, List<String> group, List<String> top10) {
            for (KeywordSet keyword : keywords) {
                if (TIME.contains(keyword)) {
                    time.add(keyword.getKorean());
                } else if (SEASON.contains(keyword)) {
                    season.add(keyword.getKorean());
                } else if (COST.contains(keyword)) {
                    cost.add(keyword.getKorean());
                } else if (GROUP.contains(keyword)) {
                    group.add(keyword.getKorean());
                } else if (TOP10.contains(keyword)) {
                    top10.add(keyword.getKorean());
                }
            }
        }
    }
}

