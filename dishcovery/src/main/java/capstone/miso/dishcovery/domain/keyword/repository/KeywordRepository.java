package capstone.miso.dishcovery.domain.keyword.repository;


import capstone.miso.dishcovery.domain.keyword.Keyword;
import capstone.miso.dishcovery.domain.keyword.KeywordSet;
import capstone.miso.dishcovery.domain.store.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-03
 * description   :
 **/

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findByStore_Sid(Long sid);
    @Query("SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keywordSet")
    List<Long> findStoreIdByKeyword(KeywordSet keywordSet, Pageable pageable);
}
