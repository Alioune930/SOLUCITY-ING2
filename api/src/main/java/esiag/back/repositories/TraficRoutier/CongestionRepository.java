package esiag.back.repositories.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Congestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CongestionRepository extends JpaRepository<Congestion, Long> {

    Optional<Congestion> findByTronconId(Long tronconId);

    List<Congestion> findByTronconIdIn(Collection<Long> tronconIds);
}