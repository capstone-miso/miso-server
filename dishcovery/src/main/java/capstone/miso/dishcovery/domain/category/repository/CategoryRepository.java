package capstone.miso.dishcovery.domain.category.repository;


import capstone.miso.dishcovery.domain.category.Category;
import capstone.miso.dishcovery.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-03
 * description   :
 **/

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByStore(Store store);
}
