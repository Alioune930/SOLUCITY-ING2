package esiag.back.repositories.Pollen;

import esiag.back.models.Pollen.dto.CapteurPollenLight;
import esiag.back.models.Pollen.entity.CapteurPollen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CapteurPollenRepository extends JpaRepository<CapteurPollen, String> {

    @Query(value =
            "SELECT cp.id_capteur AS idCapteur, cp.ville FROM capteur_pollen cp " +
            "JOIN zone_ville zv ON zv.id_capteur_pollen = cp.id_capteur " +
            "WHERE zv.id = :zoneId", nativeQuery = true)
    List<CapteurPollenLight> findCapteurPollenInZone(Long zoneId);
}