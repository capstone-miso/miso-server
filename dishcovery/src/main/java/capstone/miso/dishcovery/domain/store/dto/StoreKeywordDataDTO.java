package capstone.miso.dishcovery.domain.store.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreKeywordDataDTO {
    @Schema(description = "전체 방문 횟수")
    private long totalVisited;
    @Schema(description = "전체 사용 금액")
    private long totalCost;
    @Schema(description = "1인당 사용 금액")
    private long costPerPerson;
    @Schema(description = "전체 방문자 수")
    private long totalParticipants;
    private long spring;
    private long summer;
    private long fall;
    private long winter;
    @Schema(description = "전체 계절 방문 수")
    private long totalSeason;
    private long breakfast;
    private long lunch;
    private long dinner;
    @Schema(description = "전체 식사 시간대 방문 수")
    private long totalMeal;
    private long smallGroup;
    private long mediumGroup;
    private long largeGroup;
    private long extraGroup;
    @Schema(description = "전체 그룹대 별 방문 수")
    private long totalGroup;
    private long costUnder8000;
    private long costUnder15000;
    private long costUnder25000;
    private long costOver25000;
    @Schema(description = "금액대 전체 방문 수")
    private long costDistribution;
    public void initTotal(){
        this.totalSeason = this.spring + this.summer + this.fall + this.winter;
        this.totalMeal = this.breakfast + this.lunch + this.dinner;
        this.totalGroup = this.smallGroup + this.mediumGroup + this.largeGroup + this.extraGroup;
        this.costDistribution = this.costUnder8000 + this.costUnder15000 + this.costUnder25000 + this.costOver25000;
    }
}
