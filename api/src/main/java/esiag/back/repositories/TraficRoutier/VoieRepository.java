package esiag.back.repositories.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Voie;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoieRepository extends JpaRepository<Voie, Long> {

    List<Voie> findByTronconId(Long tronconId);

}