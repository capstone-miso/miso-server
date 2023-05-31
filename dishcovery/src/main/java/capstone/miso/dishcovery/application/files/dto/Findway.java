package capstone.miso.dishcovery.application.files.dto;
import capstone.miso.dishcovery.domain.parkinglot.dto.ParkingDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Findway {
    private Subway subway;
    private Bus bus;
    @Schema(description = "공영 주차장 정보")
    private ParkingDTO parkingZone;
}
