package capstone.miso.dishcovery.domain.image.service;

import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

}