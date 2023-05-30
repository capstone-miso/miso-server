package capstone.miso.dishcovery.application.files.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class KakaoGetStoreImgServiceTest {
    @Autowired
    private KakaoGetStoreImgService kakaoGetStoreImgService;

    @Test
    @DisplayName("카카오를 통해 매장의 메인 이미지 저장")
    public void saveStoreMainImgTest() {
        kakaoGetStoreImgService.saveStoreMainPhoto();;
    }
}