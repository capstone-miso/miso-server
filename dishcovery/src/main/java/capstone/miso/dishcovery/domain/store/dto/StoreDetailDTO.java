package capstone.miso.dishcovery.domain.store.dto;

import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-05-03
 * description   :
 **/
import capstone.miso.dishcovery.domain.menu.dto.MenuDTO;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDetailDTO {
        private Long sid;
        private String name;
        private Double lat;
        private Double lon;
        private String phone;
        private String address;
        private String category;
        private String sector;
        private List<String> onInfo;
        private List<String> offInfo;
        private List<String> keywords;
        private List<MenuDTO> menus;
}
