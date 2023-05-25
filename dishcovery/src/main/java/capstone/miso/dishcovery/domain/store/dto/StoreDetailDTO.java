package capstone.miso.dishcovery.domain.store.dto;

import capstone.miso.dishcovery.domain.keyword.dao.KeywordGroupDTO;
import capstone.miso.dishcovery.domain.menu.dto.MenuDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    @Schema(description = "가게 전화번호")
    private String phone;
    @Schema(description = "가게 주소")
    private String address;
    @Schema(description = "가게 카테고리")
    private String category;
    @Schema(description = "가게 구역")
    private String sector;
    @Schema(description = "가게 메인 이미지")
    private String mainImage;
    @Schema(description = "나의 가게 선호 여부")
    private boolean preference;
    @Schema(description = "매장 오픈 타임 정보")
    private List<String> onInfo;
    @Schema(description = "매장 종료 타임 정보")
    private List<String> offInfo;
    @Schema(description = "매장 키워드")
    private KeywordGroupDTO keywords;
    @Schema(description = "매장 이미지")
    private List<String> images;
    @Schema(description = "매장 메뉴 정보")
    @Builder.Default
    private List<MenuDTO> menus = new ArrayList<>();
    @Schema(description = "매장 방문 시간 정보")
    private StoreTimeTableDTO visitedTime;
    @Schema(description = "매장 키워드 데이터")
    private StoreKeywordDataDTO keywordData;
}
