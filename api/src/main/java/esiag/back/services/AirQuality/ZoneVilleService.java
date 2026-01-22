package esiag.back.services.AirQuality;

import com.fasterxml.jackson.databind.ObjectMapper;
import esiag.back.repositories.AirQuality.ZoneVilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ZoneVilleService {

    @Autowired
    private ZoneVilleRepository zoneVilleRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> getZonesGeoJson() {

        List<Object[]> rows = zoneVilleRepository.findAllZonesGeoJson();
        List<Map<String, Object>> features = new ArrayList<>();

        for (Object[] row : rows) {
            Long id = ((Number) row[0]).longValue();
            String ville = (String) row[1];
            String geometryJson = (String) row[2];

            Map<String, Object> feature = new HashMap<>();
            feature.put("type", "Feature");
            feature.put("id", id);

            Map<String, Object> properties = new HashMap<>();
            properties.put("ville", ville);

            feature.put("properties", properties);
            properties.put("score_pollution", 75);
            properties.put("libelle_pollution", "Moyen");
            properties.put("couleur", "#FFA500");

            try {
                feature.put("geometry", objectMapper.readValue(geometryJson, Map.class));
            } catch (Exception e) {
                throw new RuntimeException("Invalid GeoJSON geometry", e);
            }

            features.add(feature);
        }

        Map<String, Object> collection = new HashMap<>();
        collection.put("type", "FeatureCollection");
        collection.put("features", features);

        return collection;
    }
}
