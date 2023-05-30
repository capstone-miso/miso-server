package capstone.miso.dishcovery.domain.keyword.repository;

import capstone.miso.dishcovery.domain.keyword.KeywordData;
import capstone.miso.dishcovery.domain.keyword.KeywordSet;
import capstone.miso.dishcovery.domain.store.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KeywordDataRepository extends JpaRepository<KeywordData, Long> {
    @Query(value = "SELECT kd.storeId FROM KeywordData kd WHERE kd.store = :store")
    Long findIdByStore(@Param("store") Store store);
    List<KeywordData> findByStoreIsNotNull();
    @Query(value = "SELECT kd.storeId FROM KeywordData kd WHERE kd.storeId IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword)")
    List<Long> findStoreByKeyword(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    Optional<KeywordData> findTopByStore(Store store);
    @Query(value = "SELECT kd.storeId FROM KeywordData kd WHERE kd.storeId IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.breakfast DESC ")
    Page<Long> findStoreOrderByBreakfast(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.storeId FROM KeywordData kd WHERE kd.storeId IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.lunch DESC ")
    Page<Long> findStoreOrderByLunch(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.storeId FROM KeywordData kd WHERE kd.storeId IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.dinner DESC ")
    Page<Long> findStoreOrderByDinner(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.storeId FROM KeywordData kd WHERE kd.storeId IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.costUnder8000 DESC ")
    Page<Long> findStoreOrderByCostUnder8000(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.storeId FROM KeywordData kd WHERE kd.storeId IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.costUnder15000 DESC ")
    Page<Long> findStoreOrderByCostUnder15000(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.storeId FROM KeywordData kd WHERE kd.storeId IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.costUnder25000 DESC ")
    Page<Long> findStoreOrderByCostUnder25000(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.storeId FROM KeywordData kd WHERE kd.storeId IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.costOver25000 DESC ")
    Page<Long> findStoreOrderByCostOver25000(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.storeId FROM KeywordData kd WHERE kd.storeId IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.smallGroup DESC ")
    Page<Long> findStoreOrderBySmallGroup(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.storeId FROM KeywordData kd WHERE kd.storeId IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.mediumGroup DESC ")
    Page<Long> findStoreOrderByMediumGroup(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.storeId FROM KeywordData kd WHERE kd.storeId IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.largeGroup DESC ")
    Page<Long> findStoreOrderByLargeGroup(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.storeId FROM KeywordData kd WHERE kd.storeId IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.extraGroup DESC ")
    Page<Long> findStoreOrderByExtraGroup(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.storeId FROM KeywordData kd WHERE kd.storeId IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.spring DESC ")
    Page<Long> findStoreOrderBySpring(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.storeId FROM KeywordData kd WHERE kd.storeId IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.summer DESC ")
    Page<Long> findStoreOrderBySummer(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.storeId FROM KeywordData kd WHERE kd.storeId IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.fall DESC ")
    Page<Long> findStoreOrderByFall(@Param("keyword") KeywordSet keywordSet, Pageable pageable);
    @Query(value = "SELECT kd.storeId FROM KeywordData kd WHERE kd.storeId IN (SELECT k.store.sid FROM Keyword k WHERE k.keyword = :keyword) ORDER BY kd.winter DESC ")
    Page<Long> findStoreOrderByWinter(@Param("keyword") KeywordSet keywordSet, Pageable pageable);

    List<KeywordData> findByOrderByTotalCostDesc(Pageable pageable);
    List<KeywordData> findByOrderByTotalParticipantsDesc(Pageable pageable);
    List<KeywordData> findByOrderByTotalVisitedDesc(Pageable pageable);
}
