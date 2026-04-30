package esiag.back.services.HealthProfile;

import com.fasterxml.jackson.databind.ObjectMapper;
import esiag.back.models.HealthProfile.HealthProfil;
import esiag.back.models.HealthProfile.dto.ScoreHealthProfileZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ZoneHealthProfileService {

    @Autowired
    private ScoreHealthProfileService scoreHealthProfileService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> getZonesGeoJson(HealthProfil profil) {

        List<ScoreHealthProfileZone> scores = scoreHealthProfileService.calculeScoresParZone(profil);
        List<Map<String, Object>> features = new ArrayList<>();

        for (ScoreHealthProfileZone shpz : scores) {

            Map<String, Object> feature = new HashMap<>();
            feature.put("type", "Feature");

            String[] libelleCouleurPollution = calculLibelleCouleurPollution(shpz.getScorePollutionAjuste());
            String[] libelleCouleurPollen    = calculLibelleCouleurPollen(shpz.getScorePollenAjuste());

            Map<String, Object> properties = new HashMap<>();
            properties.put("idZone", shpz.getIdZone());
            properties.put("score_pollution_ajuste", shpz.getScorePollutionAjuste());
            properties.put("score_pollen_ajuste", shpz.getScorePollenAjuste());
            properties.put("score_global", shpz.getScoreRisqueGlobal());
            properties.put("libelle_pollution", libelleCouleurPollution[0]);
            properties.put("couleur_pollution", libelleCouleurPollution[1]);
            properties.put("libelle_pollen", libelleCouleurPollen[0]);
            properties.put("couleur_pollen", libelleCouleurPollen[1]);
            properties.put("date", shpz.getDateMesure());

            feature.put("properties", properties);

            try {
                if (shpz.getGeom() != null) {
                    feature.put("geometry", objectMapper.readValue(shpz.getGeom(), Map.class));
                } else {
                    feature.put("geometry", null);
                }
            } catch (Exception e) {
                System.err.println("Erreur géométrie zone " + shpz.getIdZone() + ": " + e.getMessage());
                feature.put("geometry", null);
            }

            features.add(feature);
        }

        Map<String, Object> collection = new HashMap<>();
        collection.put("type", "FeatureCollection");
        collection.put("features", features);

        return collection;
    }

    private String[] calculLibelleCouleurPollution(float score) {
        return esiag.back.services.AirQuality.calcul.PollutionCalculator.calculLibelleCouleur(score);
    }

    private String[] calculLibelleCouleurPollen(double score) {
        return esiag.back.services.Pollen.calcul.PollenCalculator.calculLibelleCouleur(score);
    }

}
