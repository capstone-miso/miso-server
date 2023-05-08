package capstone.miso.dishcovery.domain.department;

/**
 * author        : duckbill413
 * date          : 2023-05-03
 * description   :
 **/
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Entity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Department {
    @Id
    private String departmentName;
    private Double lat;
    private Double lon;
}
