package capstone.miso.dishcovery.dto;

import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-14
 * description   :
 **/

public record StoreImgDto(
        String placeName,
        String type,
        String basis,
        String moreId,
        List<String> imageUrl
) {
}
