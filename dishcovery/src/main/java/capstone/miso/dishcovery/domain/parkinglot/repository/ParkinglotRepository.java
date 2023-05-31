package capstone.miso.dishcovery.domain.parkinglot.repository;

import capstone.miso.dishcovery.domain.parkinglot.Parkinglot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkinglotRepository extends JpaRepository<Parkinglot, String> {
}
