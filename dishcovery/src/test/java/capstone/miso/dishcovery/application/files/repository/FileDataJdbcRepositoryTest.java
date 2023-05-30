package capstone.miso.dishcovery.application.files.repository;

import capstone.miso.dishcovery.domain.keyword.KeywordData;
import capstone.miso.dishcovery.domain.keyword.KeywordManager;
import capstone.miso.dishcovery.domain.keyword.dao.KeywordDataDAO;
import capstone.miso.dishcovery.domain.keyword.dao.KeywordManagerDAO;
import capstone.miso.dishcovery.domain.keyword.repository.KeywordDataRepository;
import capstone.miso.dishcovery.domain.keyword.repository.KeywordManagerRepository;
import capstone.miso.dishcovery.domain.keyword.repository.KeywordRepository;
import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    private KeywordManagerRepository keywordManagerRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Test
    @DisplayName("update all KeywordData test")
    public void updateKeywordDataTest() {
        List<KeywordDataDAO> keywordDataDAOS = fileDataJdbcRepository.getAllKeywordDataFromFileData();

        int count = 0;
        List<KeywordData> keywordDataList = new ArrayList<>();
        for (KeywordDataDAO keywordDataDAO : keywordDataDAOS) {
            if (++count % 1000 == 0){
                System.out.println("KeywordData extract count: " + count);
                keywordDataRepository.saveAll(keywordDataList);
                keywordDataList = new ArrayList<>();
            }
            KeywordData keywordData = modelMapper.map(keywordDataDAO, KeywordData.class);
            Store store = storeRepository.findById(keywordData.getStoreId()).orElseThrow();
            keywordData.setStore(store);
            keywordDataList.add(keywordData);
        }
        keywordDataRepository.saveAll(keywordDataList);
    }

    @Test
    @DisplayName("KeywordData Manager insert test")
    public void insertKeywordDataManagerTest() {
        KeywordManagerDAO fileData = fileDataJdbcRepository.getKeywordManagerFromFileData();
        KeywordManager map = modelMapper.map(fileData, KeywordManager.class);

        keywordManagerRepository.save(map);
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