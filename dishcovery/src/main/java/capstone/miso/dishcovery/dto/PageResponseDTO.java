package capstone.miso.dishcovery.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-03
 * description   :
 **/
@Getter
@ToString
@Schema(description = "Page Response DTO")
public class PageResponseDTO<T> {
    @Schema(description = "페이지 번호")
    private int page;
    @Schema(description = "한 페이지당 데이터의 수")
    private int size;
    @Schema(description = "전체 데이터의 개수")
    private int total;
    @Schema(description = "시작 페이지 번호")
    private int start; // 시작 페이지 번호
    @Schema(description = "마지막 페이지 번호")
    private int end; // 마지막 페이지 번호
    @Schema(description = "이전 페이지 존재 여부")
    private boolean prev;
    @Schema(description = "다음 페이지 존재 여부")
    private boolean next;
    @Schema(description = "데이터 리스트")
    private List<T> dtoList;
    @Builder
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<T> dtoList, int total){
        if (total <= 0) {
            this.start = 0;
            this.end = 0;
            this.prev = this.next = false;
            return;
        }

        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;

        this.dtoList = dtoList;

//        this.end = (int)(Math.ceil(this.page / 10.0)) * 10; // 화면에서의 마지막 번호 (10개 단위)
//        this.start = this.end -9; // 화면에서의 시작 번호
        this.end = (int)(Math.ceil(this.total / (double) this.size)); // 전체 가능한 페이지 개수
        this.start = 1;

//        int last = (int) (Math.ceil((total / (double) size)));
//        this.end = end > last ? last : end;
//        this.prev = this.start > 1;
//        this.next = total > this.end * this.size;
        this.prev = this.page > 1;
        this.next = this.page < this.end;
    }
}
