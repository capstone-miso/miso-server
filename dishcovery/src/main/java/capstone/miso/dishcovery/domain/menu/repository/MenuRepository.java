package capstone.miso.dishcovery.domain.menu.repository;

import capstone.miso.dishcovery.domain.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author        : duckbill413
 * date          : 2023-05-03
 * description   :
 **/

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
