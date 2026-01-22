package esiag.back.repositories.AirQuality;

import esiag.back.models.AirQuality.dto.CapteurPollutionLight;
import esiag.back.models.AirQuality.entity.CapteurPollution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CapteurPollutionRepository extends JpaRepository<CapteurPollution, String> {

    @Query(value = " SELECT cp.id_capteur AS IdCapteurPollution , cp.ville " +
    " FROM capteur_pollution cp JOIN zone_ville zv ON zv.id_capteur = cp.id_capteur " +
    " WHERE zv.id = :zoneId ", nativeQuery = true)
    List<CapteurPollutionLight> findCapteurPollutionInZone(Long zoneId);

}