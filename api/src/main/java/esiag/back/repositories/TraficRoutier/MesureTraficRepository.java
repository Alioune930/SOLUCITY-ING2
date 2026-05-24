package esiag.back.repositories.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.MesureTrafic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MesureTraficRepository extends JpaRepository<MesureTrafic, Long> {
}