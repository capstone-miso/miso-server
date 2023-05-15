package capstone.miso.dishcovery.domain.keyword.repository;

import capstone.miso.dishcovery.domain.keyword.KeywordManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordManagerRepository extends JpaRepository<KeywordManager, Long> {
    KeywordManager findTopByOrderByCreatedAtDesc();
}
