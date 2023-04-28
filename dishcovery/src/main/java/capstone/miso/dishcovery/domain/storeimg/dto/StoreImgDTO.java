package capstone.miso.dishcovery.domain.storeimg.dto;

import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-14
 * description   :
 **/

public record StoreImgDTO(
        String placeName,
        String type,
        String basis,
        String moreId,
        List<String> imageUrl
) {
}
