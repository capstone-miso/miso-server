package capstone.miso.dishcovery.domain.store.dto;

import java.util.Arrays;
import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/

public record StoreSearchCondition(
        List<Long> storeId,
        String storeName,
        String category,
        String keyword,
        String sector,
        Double lat,
        Double lon,
        Double multi
) {
    public StoreSearchCondition(Long... storeId) {
        this(Arrays.stream(storeId).toList(), null, null, null, null, null, null, 1.0);
    }
    public StoreSearchCondition(String storeName){
        this(null, storeName, null, null, null, null, null, 1.0);
    }

    public StoreSearchCondition(List<Long> storeIds) {
        this(storeIds, null, null, null, null, null, null, null);
    }
}
