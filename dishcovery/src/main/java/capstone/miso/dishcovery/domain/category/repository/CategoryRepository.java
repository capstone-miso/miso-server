package capstonedishcovery.data.domain.category.repository;

import capstonedishcovery.data.domain.category.Category;
import capstonedishcovery.data.domain.store.Store;
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
