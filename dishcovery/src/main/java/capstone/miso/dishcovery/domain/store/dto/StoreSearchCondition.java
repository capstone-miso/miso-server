package capstone.miso.dishcovery.domain.store.dto;

/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/

public record StoreSearchCondition(
        Long storeId,
        String storeName,
        String category,
        String keyword,
        String sector,
        Double lat,
        Double lon,
        Double multi
) {
    public StoreSearchCondition(Long storeId) {
        this(storeId, null, null, null, null, null, null, null);
    }
    public StoreSearchCondition(String storeName){
        this(null, storeName, null, null, null, null, null, 1.0);
    }
}
