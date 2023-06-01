package capstone.miso.dishcovery.application.files.repository;

import capstone.miso.dishcovery.domain.keyword.KeywordData;
import capstone.miso.dishcovery.domain.keyword.dao.KeywordDataDAO;
import capstone.miso.dishcovery.domain.keyword.repository.KeywordDataRepository;
import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class FileDataJdbcRepositoryTest {
    @Autowired
    private FileDataJdbcRepository fileDataJdbcRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private KeywordDataRepository keywordDataRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Test
    @DisplayName("save all KeywordData test")
    public void updateKeywordDataTest() {
        fileDataJdbcRepository.saveAllKeywordDataFromFileData();
    }

    @Test
    @DisplayName("Today KeywordDate update")
    public void updateTodayKeywordDataTest() {
        List<KeywordDataDAO> keywordDataDAOS = fileDataJdbcRepository.getKeywordDataFromFileData();
        List<KeywordData> keywordDataList = new ArrayList<>();
        keywordDataDAOS.forEach(keywordDataDAO -> {
            KeywordData keywordData = modelMapper.map(keywordDataDAO, KeywordData.class);
            keywordData.setStoreId(keywordDataDAO.getStoreId());
            keywordDataList.add(keywordData);
        });
        keywordDataRepository.saveAll(keywordDataList);
    }
}