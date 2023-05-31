package capstone.miso.dishcovery.domain.store.dto;

/**
 * author        : duckbill413
 * date          : 2023-04-27
 * description   :
 **/
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "간단한 가게 정보 응답 DTO")
public class StoreShortDTO {
        @Schema(description = "가게 ID")
        private Long id;
        @Schema(description = "가게 이름")
        private String storeName;
        @Schema(description = "가게 주소")
        private String address;
        @Schema(description = "전화번호")
        private String phone;
        @Schema(description = "위도")
        private Double lat;
        @Schema(description = "경도")
        private Double lon;
        @Schema(description = "가게 카테고리")
        private String category;
        @Schema(description = "가게 카테고리 그룹")
        private String categoryGroup;
        @Schema(description = "가게 키워드")
        private List<String> keywords;
        @Schema(description = "관심 매장 등록 횟수")
        private long preferenceCount;
        @Schema(description = "방문 횟수")
        private long totalVisit;
        @Schema(description = "총 이용 금액")
        private long totalCost;
        @Schema(description = "가게 구역")
        private String sector;
        @Schema(description = "가게 메인 이미지")
        private String mainImage;
        @Schema(description = "나의 가게 선호 여부", defaultValue = "false")
        private boolean preference;
}
