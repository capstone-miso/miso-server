package capstonedishcovery.data.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-03
 * description   :
 **/
@Getter
@ToString
public class PageResponseDTO<T> {
    private int page;
    private int size;
    private int total;
    private int start; // 시작 페이지 번호
    private int end; // 마지막 페이지 번호
    private boolean prev;
    private boolean next;
    private List<T> dtoList;
    @Builder
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<T> dtoList, int total){
        if (total <= 0)
            return;

        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();

        this.total = total;
        this.dtoList = dtoList;

        this.end = (int)(Math.ceil(this.page / 10.0)) * 10; // 화면에서의 마지막 번호
        this.start = this.end -9; // 화면에서의 시작 번호

        int last = (int) (Math.ceil((total / (double) size)));
        this.end = end > last ? last : end;

        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}
