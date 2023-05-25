package capstone.miso.dishcovery.domain.keyword.dao;

import capstone.miso.dishcovery.domain.keyword.KeywordSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static capstone.miso.dishcovery.domain.keyword.KeywordSet.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordGroupDTO {
    private List<String> time;
    private List<String> season;
    private List<String> cost;
    private List<String> group;
    private List<String> top10;

    public KeywordGroupDTO(List<KeywordSet> keywords) {
        this.time = new ArrayList<>();
        this.season = new ArrayList<>();
        this.cost = new ArrayList<>();
        this.group = new ArrayList<>();
        this.top10 = new ArrayList<>();

        KeywordGroupDTOUtil.setKeywordSet(keywords, time, season, cost, group, top10);
    }

    private static class KeywordGroupDTOUtil {
        public static final ArrayList<KeywordSet> TIME = new ArrayList<>(Arrays.asList(BREAKFAST, LUNCH, DINNER));
        public static final ArrayList<KeywordSet> SEASON = new ArrayList<>(Arrays.asList(SPRING, SUMMER, FALL, WINTER, FOUR_SEASONS));
        public static final ArrayList<KeywordSet> COST = new ArrayList<>(Arrays.asList(UNDER_COST_8000, UNDER_COST_15000, UNDER_COST_25000, OVER_COST_25000));
        public static final ArrayList<KeywordSet> GROUP = new ArrayList<>(Arrays.asList(UNDER_PARTICIPANTS_5, UNDER_PARTICIPANTS_10, UNDER_PARTICIPANTS_20, OVER_PARTICIPANTS_20));
        public static final ArrayList<KeywordSet> TOP10 = new ArrayList<>(Arrays.asList(HIGH_TOTAL_COST, HIGH_TOTAL_PARTICIPANTS, HIGH_TOTAL_VISITED));

        public static void setKeywordSet(List<KeywordSet> keywords, List<String> time, List<String> season, List<String> cost, List<String> group, List<String> top10) {
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
