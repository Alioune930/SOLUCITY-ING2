package esiag.back.controllers.Pollen;

import esiag.back.services.Pollen.ZonePollenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("zones-pollen")
public class ZonePollenController {

    @Autowired
    private ZonePollenService zonePollenService;

    @GetMapping
    public Map<String, Object> getZones() {
        return zonePollenService.getZonesGeoJson();
    }
}