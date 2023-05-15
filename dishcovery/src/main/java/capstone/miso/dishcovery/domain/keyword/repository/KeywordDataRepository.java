package capstone.miso.dishcovery.domain.keyword.repository;

import capstone.miso.dishcovery.domain.keyword.KeywordData;
import capstone.miso.dishcovery.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KeywordDataRepository extends JpaRepository<KeywordData, Long> {
    @Query(value = "SELECT kd.kid FROM KeywordData kd WHERE kd.store = :store")
    Long findIdByStore(@Param("store") Store store);
    List<KeywordData> findByStoreIsNotNull();
}
