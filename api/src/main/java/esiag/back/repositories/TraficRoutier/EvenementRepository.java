package esiag.back.repositories.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {
}