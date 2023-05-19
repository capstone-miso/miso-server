package capstone.miso.dishcovery.domain.store.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreTimeTableDTO {
    private Long until8;
    private Long hour9;
    private Long hour10;
    private Long hour11;
    private Long hour12;
    private Long hour13;
    private Long hour14;
    private Long hour15;
    private Long hour16;
    private Long hour17;
    private Long hour18;
    private Long hour19;
    private Long hour20;
    private Long hour21;
    private Long after22;
}
