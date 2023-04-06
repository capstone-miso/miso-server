package capstonedishcovery.data.domain.storeimg;

/**
 * author        : duckbill413
 * date          : 2023-03-29
 * description   :
 **/
import capstonedishcovery.data.domain.BaseEntity;
import capstonedishcovery.data.domain.store.Store;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StoreImg extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid;
    private String storeImg;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
