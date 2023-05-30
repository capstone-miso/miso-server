package capstone.miso.dishcovery.domain.keyword.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KeywordServiceTest {
    @Autowired
    private KeywordService keywordService;

    @Test
    @DisplayName("키워드 데이터로 부터 키워드 추출")
    public void extractKeywordTest() {
        keywordService.extractKeywordFromKeywordData();
    }
}