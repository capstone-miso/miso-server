package capstone.miso.dishcovery.domain.keyword.repository;


import capstone.miso.dishcovery.domain.keyword.Keyword;
import capstone.miso.dishcovery.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-03
 * description   :
 **/

public interface CategoryRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findByStore(Store store);
}
