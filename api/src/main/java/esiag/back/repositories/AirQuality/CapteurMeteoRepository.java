package esiag.back.repositories.AirQuality;

import esiag.back.models.AirQuality.dto.CapteurMeteoLight;
import esiag.back.models.AirQuality.entity.CapteurMeteo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CapteurMeteoRepository extends JpaRepository<CapteurMeteo, String> {
    @Query(value = "SELECT cm.id_capteur cm.ville FROM capteur_meteo cm " +
            " JOIN zone_ville zv ON zv.id = :zoneId ORDER BY cm.geom <-> zv.geom " +
            " LIMIT 1 ", nativeQuery = true)
    Optional<CapteurMeteoLight> findNearestMeteoToZone(Long zoneId);
}