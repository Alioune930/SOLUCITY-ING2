package esiag.back.repositories.AirQuality;

import esiag.back.models.AirQuality.dto.DonneeMeteo;
import esiag.back.models.AirQuality.entity.MesureMeteo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MesureMeteoRepository extends JpaRepository<MesureMeteo, Long> {

    @Query(value = " SELECT vitesse_vent, temperature, humidite FROM mesure_meteo " +
            " WHERE id_capteur=: idcapteur_meteo ", nativeQuery = true)
    List<DonneeMeteo> findByIdCapteurMeteo(String IdCapteurMeteo);

}
