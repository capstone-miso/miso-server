package capstone.miso.dishcovery.domain.store.dto;

import java.util.Collections;
import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/
import capstone.miso.dishcovery.domain.member.Member;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreSearchCondition {
    private List<Long> storeId;
    private String storeName;
    private String category;
    private String keyword;
    private String sector;
    private Double lat;
    private Double lon;
    private Double multi;
    private long preference;
    private Member member;

    public void setStoreIds(List<Long> storeId) {
        this.storeId = storeId;
    }
    public void setStoreId(Long storeId) {
        if (storeId == null){
            return;
        }
        this.storeId = Collections.singletonList(storeId);
    }
}
