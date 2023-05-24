package capstone.miso.dishcovery.dto;

import capstone.miso.dishcovery.domain.store.dto.StoreSearchCondition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "페이지 요청 DTO")
public class SimplePageRequestDTO {
    @Schema(description = "페이지 번호", defaultValue = "1", nullable = true)
    @Builder.Default
    @Min(value = 1L, message = "페이지 번호는 1 이상이여야 합니다.")
    private int page = 1;
    @Schema(description = "페이지 크기", defaultValue = "10", nullable = true)
    @Builder.Default
    private int size = 10;
    @Schema(description = "위도 검색 경도와 세트", nullable = true)
    private Double lat;
    @Schema(description = "경도 검색 위도와 세트", nullable = true)
    private Double lon;
    @Schema(description = "검색 범위 설정 (1km * multi로 검색 범위 설정 가능)", nullable = true, defaultValue = "1.0", example = "1.0")
    @Builder.Default
    private Double multi = 1.0d;
    @Schema(description = "리스트 정렬방식 설정: 정렬칼럼명.asc, 정렬칼럼명.desc 으로 정렬 조건 설정 가능", defaultValue = "distance", allowableValues = {"preference.asc", "preference.desc", "updatedAt"})
    private List<String> sort;

    public Pageable getPageable(String... defaultProps) {
        if (this.sort == null) {
            this.sort = Arrays.stream(defaultProps).toList();
        }
        List<Sort.Order> orders = new ArrayList<>();
        for (String s : sort) {
            if (s.endsWith(".asc")) {
                orders.add(new Sort.Order(Sort.Direction.ASC, s.substring(0, s.length() - 4)));
            } else {
                if (s.endsWith(".desc")) {
                    orders.add(new Sort.Order(Sort.Direction.DESC, s.substring(0, s.length() - 5)));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.DESC, s));
                }
            }
        }
        Sort sortBy = Sort.by(orders);
        return PageRequest.of(this.page - 1, this.size, sortBy);
    }

    @JsonIgnore
    public StoreSearchCondition getStoreSearchCondition() {
        return StoreSearchCondition.builder()
                .lat(this.lat)
                .lon(this.lon)
                .multi(this.multi)
                .build();
    }
}
