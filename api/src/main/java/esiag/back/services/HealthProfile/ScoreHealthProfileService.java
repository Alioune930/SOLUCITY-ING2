package esiag.back.services.HealthProfile;

import esiag.back.models.AirQuality.dto.ScorePollutionZone;
import esiag.back.models.HealthProfile.HealthProfil;
import esiag.back.models.HealthProfile.dto.ScoreHealthProfileZone;
import esiag.back.models.Pollen.dto.ConcentrationPollenZone;
import esiag.back.repositories.AirQuality.ZoneVilleRepository;
import esiag.back.services.AirQuality.ScorePollutionService;
import esiag.back.services.HealthProfile.calcul.HealthProfileCalculator;
import esiag.back.services.Pollen.ScorePollenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreHealthProfileService {

    @Autowired
    private ScorePollutionService scorePollutionService;

    @Autowired
    private ScorePollenService scorePollenService;

    @Autowired
    private ZoneVilleRepository zoneVilleRepository;


    private final HealthProfileCalculator healthProfileCalculator = new HealthProfileCalculator();

    public List<ScoreHealthProfileZone> calculeScoresParZone(HealthProfil profil) {


        List<Object[]> zonesData = zoneVilleRepository.findAllZonesGeoJson();
        Map<Long, String> mapGeom = new HashMap<>();
        for(Object[] obj : zonesData) {
            mapGeom.put(((Number) obj[0]).longValue(), (String) obj[2]);
        }

        List<ScoreHealthProfileZone> results = new ArrayList<>();

        List<ScorePollutionZone> scoresPollution = scorePollutionService.calculeZonePollution();
        Map<Long, ScorePollutionZone> mapPollution = new HashMap<>();
        for (ScorePollutionZone spz : scoresPollution) {
            mapPollution.put(spz.getZoneId(), spz);
        }

        List<ConcentrationPollenZone> scoresPollen = scorePollenService.calculeConcentrationZones();
        Map<Long, ConcentrationPollenZone> mapPollen = new HashMap<>();
        for (ConcentrationPollenZone cpz : scoresPollen) {
            mapPollen.put(cpz.getIdZone(), cpz);
        }

        for (Map.Entry<Long, ScorePollutionZone> entry : mapPollution.entrySet()) {
            Long idZone = entry.getKey();
            ScorePollutionZone spz = entry.getValue();

            try {
                float scorePollutionBrut = spz.getScoreGlobalPollution();
                double scorePollenBrut = 0.0;

                ConcentrationPollenZone cpz = mapPollen.get(idZone);
                if (cpz != null) {
                    scorePollenBrut = cpz.getConcentration();
                }

                float scorePollutionAjuste = healthProfileCalculator
                        .HealthProfilScorePollution(scorePollutionBrut, profil);
                double scorePollenAjuste = healthProfileCalculator
                        .HealthProfilScorePollen(scorePollenBrut, profil);

                double scoreRisqueGlobal = Math.max(scorePollutionAjuste, scorePollenAjuste);

                ScoreHealthProfileZone shpz = new ScoreHealthProfileZone();
                shpz.setIdZone(idZone);
                shpz.setScorePollutionAjuste(scorePollutionAjuste);
                shpz.setScorePollenAjuste(scorePollenAjuste);
                shpz.setScoreRisqueGlobal(scoreRisqueGlobal);
                shpz.setGeom(mapGeom.get(idZone));
                shpz.setDateMesure(spz.getDateMesure());

                results.add(shpz);

            } catch (Exception e) {
                System.err.println("Erreur HealthProfile pour la zone " + idZone + ": " + e.getMessage());
            }
        }

        return results;
    }
}
