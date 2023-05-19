package capstone.miso.dishcovery.domain.keyword;

import capstone.miso.dishcovery.domain.BaseEntity;
import capstone.miso.dishcovery.domain.store.Store;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class KeywordData extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kid;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;
    private long totalVisited;
    private long totalCost;
    private long totalParticipants;
    private long highTotalVisited;
    private long highTotalCost;
    private long highTotalParticipants;
    private long costPerPerson;
    private long spring;
    private long summer;
    private long fall;
    private long winter;
    private long breakfast;
    private long lunch;
    private long dinner;
    private long smallGroup;
    private long mediumGroup;
    private long largeGroup;
    private long extraGroup;
    private long costUnder8000;
    private long costUnder15000;
    private long costUnder25000;
    private long costOver25000;
}
