package capstone.miso.dishcovery.application.files.service;

import capstone.miso.dishcovery.application.files.FileData;
import capstone.miso.dishcovery.application.files.Files;
import capstone.miso.dishcovery.application.files.repository.FileDataRepository;
import capstone.miso.dishcovery.application.files.repository.FileRepository;
import capstone.miso.dishcovery.application.files.search.SeoulOfficerFileComp;
import capstone.miso.dishcovery.application.files.search.SeoulOfficerFileDataComp;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class SEOULFileService {
    private final SeoulOfficerFileComp fileComp;
    private final SeoulOfficerFileDataComp fileDataComp;
    private final FileRepository fileRepository;

    public List<Files> findNowMonthFiles(int year, int month) {
        List<Files> files = fileComp.findFiles(year, month);

        files.forEach(file -> {
            boolean check = fileRepository.existsByFileNameAndFileUploaded(file.getFileName(), file.getFileUploaded());
            if (!check) {
                fileRepository.save(file);
            }
        });
        return files;
    }

    public List<Files> findTotalPeriodFiles() {
        List<Files> files = new ArrayList<>();
        for (int year = 2017; year < LocalDate.now().getYear() + 1; year++) {
            for (int month = 1; month < 13; month++) {
                List<Files> result = fileComp.findFiles(year, month);
                files.addAll(result);
            }
        }
        files.forEach(file -> {
            boolean check = fileRepository.existsByFileNameAndFileUploaded(file.getFileName(), file.getFileUploaded());
            if (!check) {
                fileRepository.save(file);
            }
        });
        return files;
    }

    public void saveFileDataFromFile() {
        Optional<List<Files>> result = fileRepository.findNotConvertedWithFileData("서울");
        List<Files> files = result.orElse(null);

        if (files == null || files.size() == 0) {
            return;
        }
        int count = 0;
        for (Files file : files) {
            List<FileData> fileData = fileDataComp.parseFileToFileData(file);
            count++;
            if (count % 1000 == 0){
                log.info("File Converting Continue... " + count);
            }
            if (fileData.size() > 0) {
                file.setConvertResult("변환 성공");
            }
            file.setConverted(true);
            fileRepository.save(file);
        }
    }
}
