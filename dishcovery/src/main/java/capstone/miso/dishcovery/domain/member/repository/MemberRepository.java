package capstone.miso.dishcovery.domain.member.repository;

import capstone.miso.dishcovery.domain.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * author        : duckbill413
 * date          : 2023-04-03
 * description   :
 **/

public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid = :mid")
    Optional<Member> getWithRoles(@Param("mid") Long mid);
    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByEmail(@Param("email") String email);
}
