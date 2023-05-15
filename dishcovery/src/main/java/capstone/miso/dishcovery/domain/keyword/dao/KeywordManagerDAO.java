package capstone.miso.dishcovery.domain.keyword.dao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordManagerDAO {
    private long totalVisited;
    private long totalCost;
    private long totalParticipants;
    private long costPerPerson;
    private long spring;
    private long summer;
    private long fall;
    private long winter;
    private long breakfast;
    private long lunch;
    private long dinner;
    private long smallGroup;
    private long mediumGroup;
    private long largeGroup;
    private long extraGroup;
    private long costUnder8000;
    private long costUnder15000;
    private long costUnder25000;
    private long costOver25000;
}
