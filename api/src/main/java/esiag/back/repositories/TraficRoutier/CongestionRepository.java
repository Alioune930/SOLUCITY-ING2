package esiag.back.repositories.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Congestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CongestionRepository extends JpaRepository<Congestion, Long> {
}