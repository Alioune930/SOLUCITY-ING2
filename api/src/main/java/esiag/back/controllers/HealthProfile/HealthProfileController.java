package esiag.back.controllers.HealthProfile;

import esiag.back.models.HealthProfile.HealthProfil;
import esiag.back.services.HealthProfile.ZoneHealthProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

@RestController
@RequestMapping("/zone-health")
public class HealthProfileController {

    @Autowired
    private ZoneHealthProfileService zoneHealthProfileService;

    @GetMapping
    public Map<String, Object> getSensitiveZones(
            @RequestParam(value = "asthme", defaultValue = "false") boolean asthme,
            @RequestParam(value = "type", defaultValue = "") String type,
            @RequestParam(value = "polluLvl", defaultValue = "") String polluLvl,
            @RequestParam(value = "pollenLvl", defaultValue = "") String pollenLvl
    ) {
        HealthProfil profil = new HealthProfil();
        profil.setAsthme(asthme);
        profil.setDeclencheur(type);
        profil.setSensibilitePollution(polluLvl.isEmpty() ? null : polluLvl);
        profil.setSensibilitePollen(pollenLvl.isEmpty() ? null : pollenLvl);

        return zoneHealthProfileService.getZonesGeoJson(profil);
    }
}
