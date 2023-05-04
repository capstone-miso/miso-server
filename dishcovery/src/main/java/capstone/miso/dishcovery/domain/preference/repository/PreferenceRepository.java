package capstone.miso.dishcovery.domain.preference.repository;


import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.preference.Preference;
import capstone.miso.dishcovery.domain.store.Store;
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
    List<Preference> findByMemberOrderByUpdatedAtDesc(Member member);
    Optional<Preference> findByMemberAndStore(Member member, Store store);
    @Query("SELECT p.store.sid FROM Preference p WHERE p.pid = :pid")
    Long findStoreIdByPreferenceKey(@Param("pid") Long pid);
}
