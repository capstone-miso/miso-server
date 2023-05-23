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
public class StoreTimeTableDTO {
    @Schema(description = "오전 8시 전(time<8)")
    private Long under8;
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
    @Schema(description = "오후 10시 이후 (time>=22)")
    private Long after22;
}
