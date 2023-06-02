package capstone.miso.dishcovery.application.files.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class KWANGJINFileServiceTest {
    @Autowired
    private KWANGJINFileService kwangjinFileService;
    @Test
    @DisplayName("파일 로드 및 다운로드 테스트")
    public void fileLoadAndDownloadTest() {
        kwangjinFileService.loadFiles(0L, null, null);

        kwangjinFileService.downloadFile();
    }
    @Test
    @DisplayName("파일 To 파일데이터 컨버팅")
    public void convertFileToFileDataTest() {
        kwangjinFileService.convertFileToFileData();
    }
}