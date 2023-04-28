package capstone.miso.dishcovery.domain.store.repository;

import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.dto.StoreShortDTO;
import capstone.miso.dishcovery.domain.store.service.StoreSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * author        : duckbill413
 * date          : 2023-04-14
 * description   :
 **/

public interface StoreRepository extends JpaRepository<Store, Long>, StoreSearch {
}
