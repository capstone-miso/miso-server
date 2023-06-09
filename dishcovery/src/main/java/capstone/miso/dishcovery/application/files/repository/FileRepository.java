package capstone.miso.dishcovery.application.files.repository;

import capstone.miso.dishcovery.application.files.Files;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * author        : duckbill413
 * date          : 2023-03-21
 * description   :
 **/

public interface FileRepository extends JpaRepository<Files, Long> {
    Optional<List<Files>> findByFileDownloaded(Boolean fileDownload);
    int countByConvertedAndUpdatedAtAfter(Boolean converted, LocalDateTime dateTime);
    @Query("SELECT f FROM Files f LEFT JOIN FETCH f.fileDataList WHERE f.converted=false AND f.convertResult is null AND f.region = :region")
    Optional<List<Files>> findNotConvertedWithFileData(@Param("region") String region);
    @Query("SELECT f FROM Files f LEFT JOIN FETCH f.fileDataList WHERE f.converted=false")
    Optional<List<Files>> findFailedConvertedWithFileData();
    @Query("SELECT f FROM Files f LEFT JOIN FETCH f.fileDataList")
    List<Files> findAllFileAndData();
    boolean existsByDepartmentAndFileUploaded(String department, LocalDate date);
    boolean existsByFileNameAndFileUploaded(String fileName, LocalDate uploadedDate);
}
