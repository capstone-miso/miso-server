package capstone.miso.dishcovery.domain.parkinglot;

import capstone.miso.dishcovery.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Parkinglot extends BaseEntity {
    @Id
    private String parkingCode;
    private String parkingName;
    private String address;
    private String phone;
    private String operationRule;
    private String parkingType;
    private Double lon;
    private Double lat;
}
