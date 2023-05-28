package capstone.miso.dishcovery.application.files.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SEOULFileServiceTest {
    @Autowired
    private SEOULFileService seoulFileService;

    @Test
    @DisplayName("File collector test")
    public void collectFileTest() {
        seoulFileService.findNowMonthFiles(2023, 5);
    }

    @Test
    @DisplayName("File to FileData test")
    public void convertFileToFileData() {
        seoulFileService.saveFileDataFromFile();
    }
}