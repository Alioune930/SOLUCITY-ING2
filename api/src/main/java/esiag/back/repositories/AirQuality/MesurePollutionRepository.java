package esiag.back.repositories.AirQuality;

import esiag.back.models.AirQuality.dto.DonneePollution;
import esiag.back.models.AirQuality.entity.MesurePollution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MesurePollutionRepository extends JpaRepository<MesurePollution, Long> {

    @Query(value = " SELECT nO2 AND pm10 AND pm25 FROM mesure_pollution " +
            " WHERE id_capteur=: idcapteur_pollution ", nativeQuery = true )
    List<DonneePollution> findByIdCapteurPollution(String IdCapteurPollution);
}
