package capstone.miso.dishcovery.domain.store.dto;

import capstone.miso.dishcovery.domain.storeimg.dto.StoreImgDTO;

/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreShortDTO {
        private Long sid;
        private String storeName;
        private Double lat;
        private Double lon;
        private String category;
        private List<String> keywords;
        private String sector;
        private String imageUrl;
}
