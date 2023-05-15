package capstone.miso.dishcovery.application.files.repository;

import capstone.miso.dishcovery.domain.keyword.Keyword;
import capstone.miso.dishcovery.domain.keyword.KeywordData;
import capstone.miso.dishcovery.domain.keyword.KeywordManager;
import capstone.miso.dishcovery.domain.keyword.KeywordSet;
import capstone.miso.dishcovery.domain.keyword.dao.KeywordDataDAO;
import capstone.miso.dishcovery.domain.keyword.dao.KeywordManagerDAO;
import capstone.miso.dishcovery.domain.keyword.repository.KeywordDataRepository;
import capstone.miso.dishcovery.domain.keyword.repository.KeywordManagerRepository;
import capstone.miso.dishcovery.domain.keyword.repository.KeywordRepository;
import capstone.miso.dishcovery.domain.store.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
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
    private KeywordRepository keywordRepository;

    @Test
    @DisplayName("KeywordData update test")
    public void updateKeywordDataTest() {
        List<KeywordDataDAO> keywordDataDAOS = fileDataJdbcRepository.getAllKeywordDataFromFileData();

        List<KeywordData> keywordDataList = new ArrayList<>();
        keywordDataDAOS.forEach(keywordDataDAO -> {
            KeywordData keywordData = modelMapper.map(keywordDataDAO, KeywordData.class);

            Store store = Store.builder().sid(keywordDataDAO.getStoreId()).build();
            Long kid = keywordDataRepository.findIdByStore(store);
            keywordData.setKid(kid);
            keywordData.setStore(store);
            keywordDataList.add(keywordData);
        });
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
    public void updateTodayKeywordDataTest() throws Exception {
        List<KeywordDataDAO> keywordDataDAOS = fileDataJdbcRepository.getKeywordDataFromFileData();
        List<KeywordData> keywordDataList = new ArrayList<>();
        keywordDataDAOS.forEach(keywordDataDAO -> {
            KeywordData keywordData = modelMapper.map(keywordDataDAO, KeywordData.class);

            Store store = Store.builder().sid(keywordDataDAO.getStoreId()).build();
            Long kid = keywordDataRepository.findIdByStore(store);
            keywordData.setKid(kid);
            keywordData.setStore(store);
            keywordDataList.add(keywordData);
        });
        keywordDataRepository.saveAll(keywordDataList);
    }

    @Test
    @DisplayName("Set Store keywords")
    public void setStoreKeywordsTest() throws Exception {
        List<KeywordData> storeKeywordData = keywordDataRepository.findByStoreIsNotNull();
//        KeywordManager topByOrderByCreatedAtDesc = keywordManagerRepository.findTopByOrderByCreatedAtDesc();

        List<Keyword> keywords = new ArrayList<>();
        for (KeywordData kd : storeKeywordData) {
            KeywordSet seasonKeyword = getSeasonKeyword(kd.getSpring(), kd.getSummer(), kd.getFall(), kd.getWinter());
            Keyword keyword = Keyword.builder().store(kd.getStore()).keyword(seasonKeyword).build();
            keywords.add(keyword);
        }
        keywordRepository.saveAll(keywords);
    }
    private KeywordSet getSeasonKeyword(long spring, long summer, long fall, long winter){
        List<Long> list = Arrays.asList(spring, summer, fall, winter);
        List<KeywordSet> keyList = Arrays.asList(KeywordSet.SPRING, KeywordSet.SUMMER, KeywordSet.FALL, KeywordSet.WINTER);
        int maxIndex = 0;
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) > list.get(maxIndex)) {
                maxIndex = i;
            }
        }
        return keyList.get(maxIndex);
    }
}