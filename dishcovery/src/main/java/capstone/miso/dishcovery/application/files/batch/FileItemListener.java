package capstone.miso.dishcovery.application.files.batch;

import capstone.miso.dishcovery.application.files.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * author        : duckbill413
 * date          : 2023-04-13
 * description   :
 **/
@Log4j2
@RequiredArgsConstructor
public class FileItemListener {
    private final FileRepository fileRepository;
    @AfterJob
    public void showJobResult(JobExecution jobExecution) {
        LocalDate date = LocalDate.now(ZoneId.systemDefault());
        LocalDateTime today = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0, 0);

        int failCnt = fileRepository.countByConvertedAndUpdatedAtAfter(false, today);
        int successCnt = fileRepository.countByConvertedAndUpdatedAtAfter(true, today);
        int total = failCnt + successCnt;

        log.info("전체 File Job 실행 결과: " + successCnt + "/" + total);
    }
}
