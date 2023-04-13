package capstone.miso.dishcovery.application.controller;

import capstone.miso.dishcovery.application.files.service.FileService;
import capstone.miso.dishcovery.dto.FileDto;
import capstone.miso.dishcovery.dto.ReqDownloadFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-12
 * description   :
 **/
@Log4j2
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "File", description = "파일 컨트롤 API")
public class FileController {
    private final FileService fileService;
    @Operation(summary = "파일 검색 및 다운로드", description = "파라미터로 page번호, 시작일, 종료일을 입력 받아 파일 검색")
    @PostMapping("/download")
    public void downloadFile(@RequestBody ReqDownloadFile reqDownloadFile){
        fileService.loadFiles(reqDownloadFile.page(), reqDownloadFile.sdate(), reqDownloadFile.edate());
        fileService.downloadFile();
    }
    @Operation(summary = "파일 변환", description = "저장된 파일을 변환 fail 파라미터 값에 따라 변환 실패 파일도 변환 가능")
    @PostMapping("/convert")
    public void convertFile(@RequestParam boolean fail){
        if (fail){
            fileService.convertFailedFileToFileData();
        }else {
            fileService.convertFileToFileData();
        }
    }
    @Operation(summary = "파일 및 파일 데이터 출력")
    @GetMapping("")
    public List<FileDto> getFileAndFileData(){
        List<FileDto> result = fileService.getFileAndFileData();
        return result;
    }
}
