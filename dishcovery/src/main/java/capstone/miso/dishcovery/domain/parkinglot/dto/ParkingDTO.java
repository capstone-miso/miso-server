package capstone.miso.dishcovery.domain.parkinglot.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingDTO {
    private String parkingName;
    private String address;
    private Double lon;
    private Double lat;
}
