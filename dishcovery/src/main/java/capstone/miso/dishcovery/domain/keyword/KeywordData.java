package capstone.miso.dishcovery.domain.keyword;

import capstone.miso.dishcovery.domain.BaseEntity;
import capstone.miso.dishcovery.domain.store.Store;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.Mergeable;

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
    @Column(name = "store_id", nullable = false)
    private Long storeId;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @MapsId
    @JoinColumn(name = "store_id")
    private Store store;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long totalVisited;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long totalCost;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long totalParticipants;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long costPerPerson;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long spring;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long summer;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long fall;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long winter;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long breakfast;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long lunch;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long dinner;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long smallGroup;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long mediumGroup;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long largeGroup;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long extraGroup;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long costUnder8000;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long costUnder15000;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long costUnder25000;
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private long costOver25000;
}
