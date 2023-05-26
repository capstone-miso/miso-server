package capstone.miso.dishcovery.domain.keyword.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class KeywordSetGroupEngDTO {
    private List<String> time;
    private List<String> season;
    private List<String> cost;
    private List<String> group;
    private List<String> top10;
}
