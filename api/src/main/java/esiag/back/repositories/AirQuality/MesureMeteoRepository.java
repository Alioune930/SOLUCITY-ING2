package esiag.back.repositories.AirQuality;

import esiag.back.models.AirQuality.dto.DonneeMeteo;
import esiag.back.models.AirQuality.entity.MesureMeteo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MesureMeteoRepository extends JpaRepository<MesureMeteo, Long> {

    @Query(value = " SELECT vitesse_vent AS VitesseVent, temperature, humidite FROM mesure_meteo " +
            " WHERE id_capteur= :idCapteurMeteo ", nativeQuery = true)
    List<DonneeMeteo> findByIdCapteurMeteo(String idCapteurMeteo);

}
