package capstonedishcovery.data.application.files.repository;

import capstonedishcovery.data.application.files.Files;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * author        : duckbill413
 * date          : 2023-03-21
 * description   :
 **/

public interface FileRepository extends JpaRepository<Files, Long> {
    Optional<Files> findByName(String name);
}
