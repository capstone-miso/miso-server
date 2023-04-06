package capstonedishcovery.data.domain.menu;

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
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mid;
    private String name;
    private Integer cost;
    private String menuImg;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
