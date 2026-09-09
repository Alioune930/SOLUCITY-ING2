package esiag.back.repositories.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Coordonnee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordonneeRepository extends JpaRepository<Coordonnee, Long> {
}