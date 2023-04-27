package capstone.miso.dishcovery.domain.store.dto;

import capstone.miso.dishcovery.domain.storeimg.dto.StoreImgDTO;

/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/

public record StoreShortDTO(
        Long sid,
        String storeName,
        Double lat,
        Double lon,
        String category,
        String keyword,
        String sector,
        String imgUrl
) {
}
