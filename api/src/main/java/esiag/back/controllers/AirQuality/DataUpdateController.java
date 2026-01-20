package esiag.back.controllers.AirQuality;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("air-quality")
public class DataUpdateController{


    @PostMapping("/update-data")
    public ResponseEntity<String> updateData() {
        System.out.println("Données mise à jour par le mock");
        return ResponseEntity.ok("Données mise à jour par le mock");
    }

}
