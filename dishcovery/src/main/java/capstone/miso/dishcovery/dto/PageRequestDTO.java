package capstone.miso.dishcovery.dto;

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
public class PageRequestDTO {
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;
    private String category;
    private String keyword;
    private String sector;
    private List<String> sort;
    public Pageable getPageable(String... defaultProps) {
        if (this.sort == null) {
            this.sort = Arrays.stream(defaultProps).toList();
        }
        List<Sort.Order> orders = new ArrayList<>();
        for (String s : sort) {
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
