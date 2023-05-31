package capstone.miso.dishcovery.domain.store.dto;

import capstone.miso.dishcovery.application.files.dto.KakaoStoreDetailDTO;
import capstone.miso.dishcovery.domain.parkinglot.dto.ParkingDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "상세 가게 정보 제공 DTO")
public class StoreDetailDTO {
    @Schema(description = "가게 ID")
    private Long id;
    @Schema(description = "가게명")
    private String storeName;
    @Schema(description = "위도")
    private Double lat;
    @Schema(description = "경도")
    private Double lon;
    @Schema(description = "매장 대표 이미지")
    private String mainImageUrl;
    @Schema(description = "가게 전화번호")
    private String phone;
    @Schema(description = "가게 주소")
    private String address;
    @Schema(description = "가게 카테고리")
    private String category;
    @Schema(description = "가게 구역")
    private String sector;
    @Schema(description = "나의 가게 선호 여부")
    private boolean preference;
    @Schema(description = "관심 매장 등록 횟수")
    private long preferenceCount;
    @Schema(description = "매장 키워드")
    private List<String> keywords;
    @Schema(description = "매장 방문 시간 정보")
    private StoreTimeTableDTO visitedTime;
    @Schema(description = "매장 키워드 데이터")
    private StoreKeywordDataDTO keywordData;
    @Schema(description = "카카오 매장 정보")
    private KakaoStoreDetailDTO storeInfo;
}
