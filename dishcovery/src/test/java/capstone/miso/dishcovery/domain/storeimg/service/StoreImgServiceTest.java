package capstone.miso.dishcovery.domain.storeimg.service;

import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-14
 * description   :
 **/
@SpringBootTest
class StoreImgServiceTest {
    @Autowired
    private StoreImgService storeImgService;
    @Autowired
    private StoreRepository storeRepository;

    @DisplayName("store img 저장 테스트")
    @Test
    public void saveStoreImgsTest() {
        Store store = Store.builder()
                .name("화양식당")
                .sid(15429981L)
                .build();

        storeImgService.saveMainStoreImages(store);

        storeRepository.save(store);
    }
}