package capstone.miso.dishcovery.application.files.service;

import capstone.miso.dishcovery.application.files.FileData;
import capstone.miso.dishcovery.application.files.Files;
import capstone.miso.dishcovery.application.files.convertor.EXCELToFileConvertor;
import capstone.miso.dishcovery.application.files.convertor.PDFToFileConvertor;
import capstone.miso.dishcovery.application.files.download.DownloadFileComponent;
import capstone.miso.dishcovery.application.files.dto.FileDTO;
import capstone.miso.dishcovery.application.files.dto.FileDataDTO;
import capstone.miso.dishcovery.application.files.repository.FileDataRepository;
import capstone.miso.dishcovery.application.files.repository.FileRepository;
import capstone.miso.dishcovery.application.files.search.GwangjinFileComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
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
public class FileService {
    private final FileRepository fileRepository;
    private final FileDataRepository fileDataRepository;
    private final DownloadFileComponent downloadFileComponent;
    private final GwangjinFileComponent gwangjinFileComponent;
    private final EXCELToFileConvertor excelToTextConvertor;
    private final PDFToFileConvertor pdfToTextConvertor;
    private final ModelMapper modelMapper;

    public void changeFileConvertedStatus(Long fid) {
        Optional<Files> result = fileRepository.findById(fid);
        Files files = result.orElseThrow();
        files.changeConvertStatus(true);
        fileRepository.save(files);
    }

    public List<Files> getFileLists() {
        List<Files> result = fileRepository.findAll();
        return result;
    }

    public void downloadFile() {
        Optional<List<Files>> result = fileRepository.findByFileDownloaded(false);
        List<Files> files = result.orElse(null);

        if (Objects.isNull(files))
            return;

        for (Files f : files) {
            try {
                Files dFile = downloadFileComponent.downloadFile(f);
                fileRepository.save(dFile);
            } catch (RuntimeException e) {
                log.error("File Download Failed");
            }
        }
    }
    @Transactional
    public void loadFiles(Long page, String sdate, String edate) {
        List<Files> files = gwangjinFileComponent.loadFile(page, sdate, edate);
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

    public void checkFileExists() {
        String department = "어르신복지과";
        LocalDate date = LocalDate.parse("2023-04-11");

        boolean b = fileRepository.existsByDepartmentAndFileUploaded(department, date);
        log.info("EXISTS?: " + b);
    }
}
