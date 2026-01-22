package esiag.back.controllers.AirQuality;

import esiag.back.models.AirQuality.dto.ScorePollutionZone;
import esiag.back.services.AirQuality.ScorePollutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("zones")
public class PollutionController {

    @Autowired
    private ScorePollutionService scorePollutionService;

    @GetMapping("/pollution")
    public List<ScorePollutionZone> getPollutionZones() {
        return scorePollutionService.calculeZonePollution();
    }


}
