package esiag.back.controllers.ApiBarreAdresse;
import esiag.back.services.ServiceBarreAdresse.AdresseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("adresse-data")

public class AdresseController {
    
private final AdresseService adresseService;

    public AdresseController(AdresseService adresseService) {
        this.adresseService = adresseService;
    }

}
