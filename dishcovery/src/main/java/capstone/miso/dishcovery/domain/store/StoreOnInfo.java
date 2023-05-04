package capstone.miso.dishcovery.domain.store;

/**
 * author        : duckbill413
 * date          : 2023-05-03
 * description   :
 **/
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StoreOnInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long infoId;
    private String info;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
