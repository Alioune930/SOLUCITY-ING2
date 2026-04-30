package esiag.back.controllers.HealthProfile;

import esiag.back.models.AirQuality.dto.ScorePollutionZone;
import esiag.back.models.HealthProfile.HealthProfil;
import esiag.back.models.HealthProfile.dto.ScoreHealthProfileZone;
import esiag.back.models.Pollen.dto.ConcentrationPollenZone;
import esiag.back.services.AirQuality.ScorePollutionService;
import esiag.back.services.HealthProfile.ScoreHealthProfileService;
import esiag.back.services.Pollen.ScorePollenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/zone-health")

public class HealthProfileReportController {

    @Autowired
    private ScoreHealthProfileService scoreHealthProfileService;
    @Autowired
    private ScorePollutionService scorePollutionService;
    @Autowired
    private ScorePollenService scorePollenService;

    @GetMapping(value = "/health-profile/report", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getHealthProfileReport() {

        HealthProfil profil = new HealthProfil();
        profil.setAsthme(true);
        profil.setDeclencheur("pollution et pollen");
        profil.setSensibilitePollution("severe");
        profil.setSensibilitePollen("forte");

        List<ScoreHealthProfileZone> zones = scoreHealthProfileService.calculeScoresParZone(profil);


        Map<Long, ScorePollutionZone> mapPollution = new HashMap<>();
        for (ScorePollutionZone spz : scorePollutionService.calculeZonePollution()) {
            mapPollution.put(spz.getZoneId(), spz);
        }

        Map<Long, ConcentrationPollenZone> mapPollen = new HashMap<>();
        for (ConcentrationPollenZone cpz : scorePollenService.calculeConcentrationZones()) {
            mapPollen.put(cpz.getIdZone(), cpz);
        }

        StringBuilder rapport = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        rapport.append("RAPPORT HEALTH PROFILE\n");
        rapport.append("Heure : ").append(LocalDateTime.now().format(formatter)).append("\n");
        rapport.append("====================================\n\n");

        rapport.append("PROFIL UTILISATEUR\n");
        rapport.append(" Asthme         = ").append(profil.isAsthme()).append("\n");
        rapport.append(" Déclencheur    = ").append(profil.getDeclencheur()).append("\n");
        rapport.append(" Sensibilité pollution  = ").append(profil.getSensibilitePollution()).append("\n");
        rapport.append(" Sensibilité pollen = ").append(profil.getSensibilitePollen()).append("\n");
        rapport.append("====================================\n\n");

        for (ScoreHealthProfileZone shpz : zones) {
            Long idZone = shpz.getIdZone();

            ScorePollutionZone spz = mapPollution.get(idZone);
            ConcentrationPollenZone cpz = mapPollen.get(idZone);

            rapport.append("ZONE ").append(idZone).append("\n");

            rapport.append("Scores bruts :\n");
            rapport.append(" Pollution brute = ").append(spz.getScoreGlobalPollution()).append("\n");
            rapport.append(" Pollen brut = ").append(cpz.getConcentration()).append("\n");

            rapport.append("Scores ajustés au profil :\n");
            rapport.append(" Pollution = ").append(shpz.getScorePollutionAjuste()).append("\n");
            rapport.append(" Pollen = ").append(shpz.getScorePollenAjuste()).append("\n");
            rapport.append("Score risque global = ").append(shpz.getScoreRisqueGlobal()).append("\n");
            rapport.append("------------------------------------\n\n");
        }

        rapport.append("Total zones calculées : ").append(zones.size()).append("\n");

        return rapport.toString();
    }
}