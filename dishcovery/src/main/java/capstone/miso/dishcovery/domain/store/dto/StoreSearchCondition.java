package capstone.miso.dishcovery.domain.store.dto;

/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/

public record StoreSearchCondition(
        String category,
        String keyword,
        String sector
) {
}
