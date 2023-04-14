package capstone.miso.dishcovery.application.files.repository;


import capstone.miso.dishcovery.application.files.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * author        : duckbill413
 * date          : 2023-04-12
 * description   :
 **/

public interface FileDataRepository extends JpaRepository<FileData, Long> {
}
