package esiag.back.repositories.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.MesureTrafic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MesureTraficRepository extends JpaRepository<MesureTrafic, Long> {

    Optional<MesureTrafic> findTopByCapteurIdOrderByDateMesureDesc(Long capteurId);

    List<MesureTrafic> findByCapteurIdInOrderByDateMesureDesc(
            Collection<Long> capteurIds
    );
}