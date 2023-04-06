package capstone.miso.dishcovery.domain.preference.repository;


import capstone.miso.dishcovery.domain.member.Member;
import capstone.miso.dishcovery.domain.preference.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * author        : duckbill413
 * date          : 2023-04-03
 * description   :
 **/

public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    List<Preference> findByMember(Member member);
}
