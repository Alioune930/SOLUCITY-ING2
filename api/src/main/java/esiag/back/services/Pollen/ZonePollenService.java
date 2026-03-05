package esiag.back.services.Pollen;

import com.fasterxml.jackson.databind.ObjectMapper;
import esiag.back.models.Pollen.dto.ConcentrationPollenZone;
import esiag.back.services.Pollen.calcul.PollenCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ZonePollenService {

    @Autowired
    private ScorePollenService scorePollenService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> getZonesGeoJson() {

        List<ConcentrationPollenZone> concentrations = scorePollenService.calculeConcentrationZones();
        List<Map<String, Object>> features = new ArrayList<>();

        for (int i = 0; i < concentrations.size(); i++) {
            ConcentrationPollenZone cpz = concentrations.get(i);
            
            Map<String, Object> feature = new HashMap<>();
            feature.put("type", "Feature");

            String[] infosCouleur = PollenCalculator.calculLibelleCouleur(cpz.getConcentration());

            Map<String, Object> properties = new HashMap<>();
            properties.put("idZone", cpz.getIdZone());
            properties.put("ville", cpz.getVille());
            properties.put("saison", cpz.getSaison());
            properties.put("temperature", cpz.getTemperature());
            properties.put("idCapteur", cpz.getIdCapteur());
            properties.put("concentration", cpz.getConcentration());
            properties.put("libelle", infosCouleur[0]);
            properties.put("codeCouleur", infosCouleur[1]);
            properties.put("timestamp", cpz.getTimestamp()); 

            feature.put("properties", properties);

            try {
                if (cpz.getGeom() != null) {

                    feature.put("geometry", objectMapper.readValue(cpz.getGeom(), Map.class));
                } else {
                    feature.put("geometry", null);
                }
            } catch (Exception e) {
                System.out.println("Erreur sur la geometrie de la zone " + cpz.getIdZone());
                feature.put("geometry", null);
            }

            features.add(feature);
        }

        Map<String, Object> collection = new HashMap<>();
        collection.put("type", "FeatureCollection");
        collection.put("features", features);

        return collection;
    }
}