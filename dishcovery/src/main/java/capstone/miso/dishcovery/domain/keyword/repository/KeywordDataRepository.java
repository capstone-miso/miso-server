package capstone.miso.dishcovery.domain.keyword.repository;

import capstone.miso.dishcovery.domain.keyword.Keyword;
import capstone.miso.dishcovery.domain.keyword.KeywordData;
import capstone.miso.dishcovery.domain.keyword.KeywordSet;
import capstone.miso.dishcovery.domain.store.Store;
import org.apache.fontbox.afm.Ligature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.security.Key;
import java.util.List;

public interface KeywordDataRepository extends JpaRepository<KeywordData, Long> {
    @Query(value = "SELECT kd.kid FROM KeywordData kd WHERE kd.store = :store")
    Long findIdByStore(@Param("store") Store store);
    List<KeywordData> findByStoreIsNotNull();
    @Query(value = "SELECT kd.store.sid FROM KeywordData kd WHERE kd.store.sid IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword)")
    List<Long> findStoreByKeyword(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.store.sid FROM KeywordData kd WHERE kd.store.sid IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.breakfast DESC ")
    List<Long> findStoreOrderByBreakfast(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.store.sid FROM KeywordData kd WHERE kd.store.sid IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.lunch DESC ")
    List<Long> findStoreOrderByLunch(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.store.sid FROM KeywordData kd WHERE kd.store.sid IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.dinner DESC ")
    List<Long> findStoreOrderByDinner(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.store.sid FROM KeywordData kd WHERE kd.store.sid IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.costUnder8000 DESC ")
    List<Long> findStoreOrderByCostUnder8000(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.store.sid FROM KeywordData kd WHERE kd.store.sid IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.costUnder15000 DESC ")
    List<Long> findStoreOrderByCostUnder15000(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.store.sid FROM KeywordData kd WHERE kd.store.sid IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.costUnder25000 DESC ")
    List<Long> findStoreOrderByCostUnder25000(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.store.sid FROM KeywordData kd WHERE kd.store.sid IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.costOver25000 DESC ")
    List<Long> findStoreOrderByCostOver25000(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.store.sid FROM KeywordData kd WHERE kd.store.sid IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.smallGroup DESC ")
    List<Long> findStoreOrderBySmallGroup(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.store.sid FROM KeywordData kd WHERE kd.store.sid IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.mediumGroup DESC ")
    List<Long> findStoreOrderByMediumGroup(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.store.sid FROM KeywordData kd WHERE kd.store.sid IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.largeGroup DESC ")
    List<Long> findStoreOrderByLargeGroup(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.store.sid FROM KeywordData kd WHERE kd.store.sid IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.extraGroup DESC ")
    List<Long> findStoreOrderByExtraGroup(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.store.sid FROM KeywordData kd WHERE kd.store.sid IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.spring DESC ")
    List<Long> findStoreOrderBySpring(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.store.sid FROM KeywordData kd WHERE kd.store.sid IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.summer DESC ")
    List<Long> findStoreOrderBySummer(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.store.sid FROM KeywordData kd WHERE kd.store.sid IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.fall DESC ")
    List<Long> findStoreOrderByFall(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.store.sid FROM KeywordData kd WHERE kd.store.sid IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.winter DESC ")
    List<Long> findStoreOrderByWinter(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
}
