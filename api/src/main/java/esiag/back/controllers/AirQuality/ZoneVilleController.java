package esiag.back.controllers.AirQuality;

import esiag.back.repositories.AirQuality.ZoneVilleRepository;
import esiag.back.services.AirQuality.ZoneVilleService;
import esiag.back.services.sample.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("zones")
public class ZoneVilleController {

    @Autowired
    private ZoneVilleService zoneVilleService;

    @GetMapping
    public Map<String, Object> getZones() {
        return zoneVilleService.getZonesGeoJson();
    }

}
