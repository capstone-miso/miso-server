package capstone.miso.dishcovery.domain.store.repository;

import capstone.miso.dishcovery.domain.store.Store;
import capstone.miso.dishcovery.domain.store.service.StoreSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-14
 * description   :
 **/
public interface StoreRepository extends JpaRepository<Store, Long>, StoreSearch {
    boolean existsBySidAndMainImageUrl(Long storeId, String mainImageUrl);
    List<Store> findByMainImageUrlIsNullOrderBySidDesc();
}
