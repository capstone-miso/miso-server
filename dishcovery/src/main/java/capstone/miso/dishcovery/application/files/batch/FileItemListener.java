package capstone.miso.dishcovery.application.files.batch;

import capstone.miso.dishcovery.application.files.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;

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
        int failCnt = fileRepository.findByConverted(false).orElse(new ArrayList<>()).size();
        int successCnt = fileRepository.findByConverted(true).orElse(new ArrayList<>()).size();
        int total = failCnt + successCnt;

        log.info("File loading Job 실행 결과: " + successCnt + "/" + total);
    }
}
