package capstone.miso.dishcovery.application.files.service;

import capstone.miso.dishcovery.application.files.FileData;
import capstone.miso.dishcovery.application.files.Files;
import capstone.miso.dishcovery.application.files.convertor.KWANGJINExcelConvertor;
import capstone.miso.dishcovery.application.files.convertor.KWANGJINPdfConvertor;
import capstone.miso.dishcovery.application.files.download.KWANGJINFileDownloader;
import capstone.miso.dishcovery.application.files.dto.FileDTO;
import capstone.miso.dishcovery.application.files.dto.FileDataDTO;
import capstone.miso.dishcovery.application.files.repository.FileRepository;
import capstone.miso.dishcovery.application.files.search.GwangjinFileComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * author        : duckbill413
 * date          : 2023-03-21
 * description   :
 **/
@Log4j2
@Service
@RequiredArgsConstructor
public class KWANGJINFileService {
    private final FileRepository fileRepository;
    private final KWANGJINFileDownloader KWANGJINFileDownloader;
    private final GwangjinFileComponent gwangjinFileComponent;
    private final KWANGJINExcelConvertor excelToTextConvertor;
    private final KWANGJINPdfConvertor pdfToTextConvertor;
    private final static String KWANGJIN = "광진구";

    public void changeFileConvertedStatus(Long fid) {
        Optional<Files> result = fileRepository.findById(fid);
        Files files = result.orElseThrow();
        files.changeConvertStatus(true);
        fileRepository.save(files);
    }
    public void downloadFile() {
        Optional<List<Files>> result = fileRepository.findByFileDownloaded(false);
        List<Files> files = result.orElse(null);

        if (Objects.isNull(files))
            return;

        int count = 0;
        for (Files f : files) {
            try {
                Files dFile = KWANGJINFileDownloader.downloadFile(f);
                count++;
                dFile.setRegion(KWANGJIN);
                fileRepository.save(dFile);
            } catch (RuntimeException e) {
                log.error("File Download Failed");
            }
        }
        log.info(LocalDate.now() + " 파일 Download 개수: " + count);
    }
    @Transactional
    public void loadFiles(Long page, String sdate, String edate) {
        List<Files> files = gwangjinFileComponent.loadFile(page, sdate, edate);
        log.info(LocalDate.now() + " 파일 Load 개수: " + files.size());
        // 이미 저장된 파일 리스트는 검색에서 제외한다.
        for (Files f : files) {
            String department = f.getDepartment();
            LocalDate date = f.getFileUploaded();
            // 값이 존재하지 않을 경우 추가 (부서명과 날짜로 검색)
            boolean result = fileRepository.existsByDepartmentAndFileUploaded(department, date);
            if (!result){
                fileRepository.save(f);
            }
        }
    }
    @Transactional
    public void convertFileToFileData() {
        Optional<List<Files>> result = fileRepository.findNotConvertedWithFileData();
        List<Files> files = result.orElse(null);

        if (Objects.isNull(files)) {
            return;
        }

        List<FileData> fileDataList = new ArrayList<>();
        for (Files f : files) {
            List<FileData> fileData = switch (f.getFileFormat()) {
                case "pdf" -> pdfToTextConvertor.parseFileToFileData(f);
                case "xls", "xlsx" -> excelToTextConvertor.parseFileToFileData(f);
                default -> {
                    f.setConverted(false);
                    f.setConvertResult("Not supported format: " + f.getFileFormat());
                    yield null;
                }
            };
            if (Objects.isNull(fileData)) {
                f.setConverted(false);
                f.setConvertResult("File Convert Failed");
                continue;
            }

            fileData.forEach(fd -> {
                if (Objects.nonNull(fd)) {
                    fileDataList.add(fd);
                }
            });
        }
        log.info(LocalDate.now() + " 파일 데이터 Convert: " + fileDataList.size());
        fileRepository.saveAll(files);
    }

    @Transactional
    public void convertFailedFileToFileData() {
        Optional<List<Files>> result = fileRepository.findFailedConvertedWithFileData();
        List<Files> files = result.orElse(null);

        if (Objects.isNull(files)) {
            return;
        }

        List<FileData> fileDataList = new ArrayList<>();
        for (Files f : files) {
            List<FileData> fileData = switch (f.getFileFormat()) {
                case "pdf" -> pdfToTextConvertor.parseFileToFileData(f);
                case "xls", "xlsx" -> excelToTextConvertor.parseFileToFileData(f);
                default -> {
                    f.setConverted(false);
                    f.setConvertResult("Not supported format: " + f.getFileFormat());
                    yield null;
                }
            };

            if (Objects.isNull(fileData))
                continue;

            fileData.forEach(fd -> {
                if (Objects.nonNull(fd)) {
                    fileDataList.add(fd);
                }
            });
        }

        fileRepository.saveAll(files);
    }

    public List<FileDTO> getFileAndFileData() {
        List<Files> result = fileRepository.findAllFileAndData();
        List<FileDTO> fileDTOS = new ArrayList<>();
        for (Files file : result) {
            List<FileDataDTO> fileDataDTOS = new ArrayList<>();
            file.getFileDataList().forEach(f -> {
                FileDataDTO tmp = new FileDataDTO(f.getFid(), f.getDate(), f.getTime(), f.getStoreName(), f.getStoreAddress(), f.getParticipants(), f.getCost());
                fileDataDTOS.add(tmp);
            });

            FileDTO fileDto = new FileDTO(file.getFid(), file.getRegion(), file.getDepartment(),
                    file.getFileName(), file.getFileUrl(), file.getFileUploaded(), file.isFileDownloaded(), file.isConverted(),
                    file.getConvertResult(), fileDataDTOS);

            fileDTOS.add(fileDto);
        }

        return fileDTOS;
    }
}
