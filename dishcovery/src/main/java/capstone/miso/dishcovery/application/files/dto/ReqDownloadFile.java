package capstone.miso.dishcovery.application.files.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/**
 * author        : duckbill413
 * date          : 2023-04-12
 * description   :
 **/

public record ReqDownloadFile (
        @Positive(message = "페이지 번호는 1이상 입니다.") Long page,
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "검색 시작 날짜를 입력해주세요(yyyy-MM-dd)") String sdate,
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "검색 시작 날짜를 입력해주세요(yyyy-MM-dd)") String edate
){
}
