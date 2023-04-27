package capstone.miso.dishcovery.domain.keyword;

/**
 * author        : duckbill413
 * date          : 2023-03-29
 * description   :
 **/
import capstone.miso.dishcovery.domain.BaseEntity;
import capstone.miso.dishcovery.domain.store.Store;
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
public class Keyword extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kid;
    private String keywordKeys;
    private String reason;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
