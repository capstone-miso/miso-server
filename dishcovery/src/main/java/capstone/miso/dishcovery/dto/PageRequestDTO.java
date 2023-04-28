package capstone.miso.dishcovery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    public Pageable getPageable(String... props){
        return PageRequest.of(this.page - 1, this.size,
                Sort.by(props).descending());
    }
}
