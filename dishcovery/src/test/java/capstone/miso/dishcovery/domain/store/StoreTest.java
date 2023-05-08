package capstone.miso.dishcovery.domain.store;

import capstone.miso.dishcovery.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * author        : duckbill413
 * date          : 2023-05-03
 * description   :
 **/
@SpringBootTest
class StoreTest {
    @Autowired
    private StoreRepository storeRepository;

    @DisplayName("store sector init test")
    @Test
    public void storeSectorInsert() {
        List<Store> stores = storeRepository.findAll();
        stores.forEach(store -> {
            if (store.getSid() != -1 && store.getSector() == null){
                store.setSector("광진구");
//            storeRepository.save(store);
            }
        });
        storeRepository.saveAll(stores);
    }
}