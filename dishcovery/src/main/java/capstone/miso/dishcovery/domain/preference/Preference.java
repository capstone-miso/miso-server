package capstonedishcovery.data.domain.preference;

/**
 * author        : duckbill413
 * date          : 2023-03-29
 * description   :
 **/
import capstonedishcovery.data.domain.BaseEntity;
import capstonedishcovery.data.domain.store.Store;
import capstonedishcovery.data.domain.member.Member;
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
public class Preference extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
