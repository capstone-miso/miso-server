package capstone.miso.dishcovery.application.files.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SEOULFileServiceTest {
    @Autowired
    private SEOULFileService seoulFileService;
    @Autowired
    private KakaoFileDataMatchService kakaoFileDataMatchService;

    @Test
    @DisplayName("Test Convert File to FileData")
    public void convertFileToFileDataTest() {
//        seoulFileService.saveFileDataFromFile();
        kakaoFileDataMatchService.fileDataStoreMatcher(10000L, 100000L);
    }
    @Test
    @DisplayName("File collector test")
    public void collectFileTest() {
        seoulFileService.findNowMonthFiles();
    }

    @Test
    @DisplayName("File to FileData test")
    public void convertFileToFileData() {
        seoulFileService.saveFileDataFromFile();
    }
}