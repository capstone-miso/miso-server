package capstonedishcovery.data.domain.member.repository;

import capstonedishcovery.data.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * author        : duckbill413
 * date          : 2023-04-03
 * description   :
 **/

public interface MemberRepository extends JpaRepository<Member, Long> {
}
