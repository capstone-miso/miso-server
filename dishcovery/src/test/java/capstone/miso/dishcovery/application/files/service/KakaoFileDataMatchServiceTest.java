package capstone.miso.dishcovery.application.files.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KakaoFileDataMatchServiceTest {
    @Autowired
    private KakaoFileDataMatchService kakaoFileDataMatchService;

    @Test
    @DisplayName("FileData Match Service test")
    public void storeExtractionTest() {
        kakaoFileDataMatchService.fileDataStoreMatcher(0L, 100000L);
    }
}