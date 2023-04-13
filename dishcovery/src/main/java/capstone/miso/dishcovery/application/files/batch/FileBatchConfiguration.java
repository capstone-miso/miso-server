package capstone.miso.dishcovery.application.files.batch;

import capstone.miso.dishcovery.application.files.repository.FileRepository;
import capstone.miso.dishcovery.application.files.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * author        : duckbill413
 * date          : 2023-04-13
 * description   :
 **/
@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableScheduling
public class FileBatchConfiguration {
    private final int CHUNK_SIZE = 500;
    private final String JOB_NAME = "fileJob";
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final JobLauncher jobLauncher;

    @Scheduled(cron = "0 46 17 * * ?")
    public void runLoadingJob() throws Exception {
        log.info("------------Scheduled-----------------------");
        jobLauncher.run(fileLoadingJob(null, null), new JobParametersBuilder()
                .addString("sdate", LocalDate.now().minusDays(1L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters());
    }

    @Bean
    public Job fileLoadingJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder(JOB_NAME, jobRepository).incrementer(new RunIdIncrementer())
                .listener(new FileItemListener(fileRepository))
                .start(fileFindStep(jobRepository, transactionManager, null, null))
                .next(fileDownloadStep(jobRepository, transactionManager))
                .next(fileConvertStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    @JobScope
    public Step fileFindStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                             @Value("#{jobParameters[sdate]}") String sdate,
                             @Value("#{jobParameters[edate]}") String edate) {
        log.info("File loading... Start DATE: " + sdate);
        return new StepBuilder(JOB_NAME + "LoadStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    fileService.loadFiles(null, sdate, edate);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step fileDownloadStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(JOB_NAME + "DownloadStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    fileService.downloadFile();
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step fileConvertStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder(JOB_NAME + "ConvertStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    fileService.convertFileToFileData();
                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }
}