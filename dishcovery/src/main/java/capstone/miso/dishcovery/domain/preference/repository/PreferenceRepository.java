package capstone.miso.dishcovery.domain.preference.repository;


import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.preference.Preference;
import capstone.miso.dishcovery.domain.store.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * author        : duckbill413
 * date          : 2023-04-03
 * description   :
 **/

public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    List<Preference> findByMember(Member member);
    @Query(value = "SELECT new capstone.miso.dishcovery.domain.preference.repository.PreferenceDAO(p.pid, p.member, p.store) " +
            "FROM Preference p " +
            "WHERE p.member = :member " +
            "GROUP BY p.store")
    Page<PreferenceDAO> findMyPreferenceStores(@Param("member") Member member, Pageable pageable);

    Optional<Preference> findByMemberAndStore(Member member, Store store);
    @Query("SELECT p.store.sid FROM Preference p WHERE p.pid = :pid")
    Long findStoreIdByPreferenceKey(@Param("pid") Long pid);
    @Query("SELECT p.store.sid FROM Preference p GROUP BY p.store order by count(p.store) DESC")
    Page<Long> findFamousStores(Pageable pageable);
    @Query("SELECT p.pid FROM Preference p WHERE p.member = :member AND p.store.sid = :sid")
    List<Long> checkMyStorePreference(@Param("member") Member member,@Param("sid") Long sid, Pageable pageable);
}
