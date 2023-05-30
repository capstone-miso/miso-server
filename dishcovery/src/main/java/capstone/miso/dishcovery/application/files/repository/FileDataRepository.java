package capstone.miso.dishcovery.application.files.repository;


import capstone.miso.dishcovery.application.files.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * author        : duckbill413
 * date          : 2023-04-12
 * description   :
 **/

public interface FileDataRepository extends JpaRepository<FileData, Long> {
    Optional<List<FileData>> findAllByStoreIsNull();
    Optional<List<FileData>> findAllByStoreIsNullAndFidBetween(Long after, Long before);
}
