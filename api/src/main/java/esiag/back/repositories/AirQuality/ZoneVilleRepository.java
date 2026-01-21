package esiag.back.repositories.AirQuality;

import esiag.back.models.AirQuality.ZoneVille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ZoneVilleRepository extends JpaRepository<ZoneVille, Long> {
    @Query(value = "SELECT id AS id, ville AS ville, ST_AsGeoJSON(geom) AS geom FROM zone_ville", nativeQuery = true)
    List<Object[]> findAllZonesGeoJson();
}
