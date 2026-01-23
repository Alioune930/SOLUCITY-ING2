package esiag.back.controllers.AirQuality;

import esiag.back.models.AirQuality.dto.*;
import esiag.back.repositories.AirQuality.CapteurMeteoRepository;
import esiag.back.repositories.AirQuality.CapteurPollutionRepository;
import esiag.back.repositories.AirQuality.MesureMeteoRepository;
import esiag.back.repositories.AirQuality.MesurePollutionRepository;
import esiag.back.services.AirQuality.ScorePollutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("zones")
public class PollutionReportController {

    @Autowired
    private ScorePollutionService scorePollutionService;
    @Autowired
    private CapteurPollutionRepository capteurPollutionRepository;
    @Autowired
    private CapteurMeteoRepository capteurMeteoRepository;
    @Autowired
    private MesurePollutionRepository mesurePollutionRepository;
    @Autowired
    private MesureMeteoRepository mesureMeteoRepository;


    @GetMapping(value = "/pollution/report", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getPollutionReport() {

        List<ScorePollutionZone> zones = scorePollutionService.calculeZonePollution();
        StringBuilder rapport = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        rapport.append("RAPPORT DE CALCUL DE LA POLLUTION\n");
        rapport.append("Heure : ").append(java.time.LocalDateTime.now().format(formatter)).append("\n");
        rapport.append("====================================\n\n");

        for (ScorePollutionZone spz : zones) {

            CapteurPollutionLight cp = capteurPollutionRepository.findCapteurPollutionInZone(spz.getZoneId()).get(0);
            CapteurMeteoLight cm = capteurMeteoRepository.findNearestMeteoToZone(spz.getZoneId()).get(0);

            DonneePollution dp = mesurePollutionRepository.findByIdCapteurPollution(cp.getIdCapteurPollution()).get(0);
            DonneeMeteo dm = mesureMeteoRepository.findByIdCapteurMeteo(cm.getIdCapteurMeteo()).get(0);

            rapport.append("ZONE ").append(spz.getZoneId()).append("\n");

            // Ajouter les données brutes complètes
            rapport.append("Données brutes :\n");
            rapport.append(" NO2 = ").append(dp.getNo2()).append("\n");
            rapport.append(" PM10 = ").append(dp.getPm10()).append("\n");
            rapport.append(" PM2.5 = ").append(dp.getPm25()).append("\n");
            rapport.append(" Vent = ").append(dm.getVitesseVent()).append(" km/h\n");
            rapport.append(" Humidité = ").append(dm.getHumidite()).append(" %\n");
            rapport.append(" Température = ").append(dm.getTemperature()).append(" °C\n\n");

            // Valeurs ajustées et sous-indices déjà calculés
            rapport.append("Valeurs ajustées :\n");
            rapport.append(" NO2 ajusté = ").append(spz.getIndiceNo2Ajuste()).append("\n");
            rapport.append(" PM10 ajusté = ").append(spz.getIndicePm10Ajuste()).append("\n");
            rapport.append(" PM2.5 ajusté = ").append(spz.getIndicePm25Ajuste()).append("\n");
            rapport.append("Sous-indices :\n");
            rapport.append(" NO2 = ").append(spz.getSousIndiceNo2()).append("%\n");
            rapport.append(" PM10 = ").append(spz.getSousIndicePm10()).append("%\n");
            rapport.append(" PM2.5 = ").append(spz.getSousIndicePm25()).append("%\n");
            rapport.append("Score global = ").append(spz.getScoreGlobalPollution()).append("\n");
            rapport.append("Qualité de l'air = ").append(spz.getLibellePollution()).append("\n");
            rapport.append("------------------------------------\n\n");
        }


        return rapport.toString();
    }


}
