package capstone.miso.dishcovery.domain.store.repository;

import capstone.miso.dishcovery.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * author        : duckbill413
 * date          : 2023-04-14
 * description   :
 **/
public interface StoreRepository extends JpaRepository<Store, Long>, StoreSearch {
}
