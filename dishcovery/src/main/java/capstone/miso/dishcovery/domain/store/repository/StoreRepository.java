package capstone.miso.dishcovery.domain.store.repository;

import capstone.miso.dishcovery.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author        : duckbill413
 * date          : 2023-04-14
 * description   :
 **/

public interface StoreRepository extends JpaRepository<Store, Long> {
}
