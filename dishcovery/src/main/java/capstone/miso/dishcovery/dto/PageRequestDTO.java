package capstone.miso.dishcovery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-03
 * description   :
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
@Schema(description = "페이지 요청 DTO")
public class PageRequestDTO {
    @Schema(description = "페이지 번호", defaultValue = "1", nullable = true)
    @Builder.Default
    @Min(value = 1L, message = "페이지 번호는 1 이상이여야 합니다.")
    private int page = 1;
    @Schema(description = "페이지 크기", defaultValue = "10", nullable = true)
    @Builder.Default
    private int size = 10;
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
    @Schema(description = "위도 검색 경도와 세트", nullable = true)
    private Double lat;
    @Schema(description = "경도 검색 위도와 세트", nullable = true)
    private Double lon;
    @Schema(description = "검색 범위 설정 (1km * multi로 검색 범위 설정 가능)", nullable = true, defaultValue = "1.0", example = "1.0")
    @Builder.Default
    private Double multi = 1.0d;
    @Schema(description = "리스트 정렬방식 설정: 정렬칼럼명.asc, 정렬칼럼명.desc 으로 정렬 조건 설정 가능", example = "storeId.desc")
    private List<String> sort;

    public Pageable getPageable(String... defaultProps) {
        if (this.sort == null) {
            this.sort = Arrays.stream(defaultProps).toList();
        }
        List<Sort.Order> orders = new ArrayList<>();
        for (String s : sort) {
            if (s.endsWith(".asc")) {
                s = s.substring(0, s.length() - 4);
            }

            if (s.endsWith(".desc")) {
                orders.add(new Sort.Order(Sort.Direction.DESC, s.substring(0, s.length() - 5)));
            } else {
                orders.add(new Sort.Order(Sort.Direction.ASC, s));
            }
        }
        Sort sortBy = Sort.by(orders);
        return PageRequest.of(this.page - 1, this.size, sortBy);
    }
}
