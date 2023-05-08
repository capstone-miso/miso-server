package capstone.miso.dishcovery.domain.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * author        : duckbill413
 * date          : 2023-05-03
 * description   :
 **/
@Schema(description = "가게 메뉴 정보 DTO")
public record MenuDTO (
        @Schema(description = "메뉴 ID") Long mid,
        @Schema(description = "메뉴명") String name,
        @Schema(description = "메뉴 가격") String cost,
        @Schema(description = "메뉴 상세 정보") String detail,
        @Schema(description = "메뉴 이미지") String menuImg
){
}
