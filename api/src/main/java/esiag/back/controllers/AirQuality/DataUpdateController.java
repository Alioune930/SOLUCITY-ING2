package esiag.back.controllers.AirQuality;

import esiag.back.services.AirQuality.ScorePollutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("air-quality")
public class DataUpdateController{

    @Autowired
    private ScorePollutionService scorePollutionService;


    @PostMapping("/update-data")
    public ResponseEntity<String> updateData() {
        scorePollutionService.calculeZonePollution();
        System.out.println("Données mise à jour par le mock et score de pollution recalculé");
        return ResponseEntity.ok("Données mise à jour par le mock et score de pollution recalculé");
    }

}
