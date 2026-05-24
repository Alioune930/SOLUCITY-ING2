package esiag.back.repositories.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Capteur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapteurRepository extends JpaRepository<Capteur, Long> {
}