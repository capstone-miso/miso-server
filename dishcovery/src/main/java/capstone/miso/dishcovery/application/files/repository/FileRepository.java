package capstonedishcovery.data.application.files.repository;

import capstonedishcovery.data.application.files.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * author        : duckbill413
 * date          : 2023-03-21
 * description   :
 **/

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByFileName(String fileName);
}