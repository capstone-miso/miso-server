package capstone.miso.dishcovery.application.files.repository;


import capstone.miso.dishcovery.application.files.FileData;
import capstone.miso.dishcovery.domain.keyword.KeywordData;
import capstone.miso.dishcovery.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-12
 * description   :
 **/

public interface FileDataRepository extends JpaRepository<FileData, Long> {
}
