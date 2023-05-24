package capstone.miso.dishcovery.dto;

import capstone.miso.dishcovery.domain.store.dto.StoreSearchCondition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * author        : duckbill413
 * date          : 2023-04-03
 * description   :
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "페이지 요청 DTO")
public class PageRequestDTO extends SimplePageRequestDTO {
    @Schema(description = "매장 ID 검색", nullable = true)
    private Long storeId;
    @Schema(description = "매장명 검색", nullable = true)
    private String storeName;
    @Schema(description = "매장 카테고리 검색", nullable = true)
    private String category;
    @Schema(description = "키워드 카테고리 검색", nullable = true)
    private String keyword;
    @Schema(description = "관할 구역 검색", nullable = true, allowableValues = {"광진구"})
    private String sector;

    @JsonIgnore
    @Override
    public StoreSearchCondition getStoreSearchCondition() {
        StoreSearchCondition condition = super.getStoreSearchCondition();
        condition.setStoreId(this.storeId);
        condition.setStoreName(this.storeName);
        condition.setCategory(this.category);
        condition.setKeyword(this.keyword);
        condition.setSector(this.sector);
        return condition;
    }
}
