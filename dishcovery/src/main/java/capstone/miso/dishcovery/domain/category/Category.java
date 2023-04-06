package capstonedishcovery.data.domain.category;

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
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;
    private String keyword; // FIXME
    private String reason;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
