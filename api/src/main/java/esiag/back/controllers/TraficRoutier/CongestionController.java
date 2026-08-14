package esiag.back.controllers.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Congestion;
import esiag.back.services.TraficRoutier.CongestionService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api/trafic/congestions")
@CrossOrigin("*")
public class CongestionController {

    private final CongestionService congestionService;

    public CongestionController(CongestionService congestionService) {
        this.congestionService = congestionService;
    }

    @GetMapping
    public List<Congestion> getAllCongestions() {
        return congestionService.getAll();
    }

    @PostMapping
    public Congestion createCongestion(@RequestBody Congestion congestion) {
        return congestionService.save(congestion);
    }

    @DeleteMapping("/{id}")
    public void deleteCongestion(@PathVariable Long id) {
        congestionService.delete(id);
    }

    @PostMapping("/recalculer")
    public void recalculer() {
        congestionService.recalculerToutesLesCongestions();
    }

    @PostMapping("/recalculer/{capteurId}")
    public void recalculerPourCapteur(@PathVariable Long capteurId) {
        congestionService.recalculerPourCapteur(capteurId);
    }

    @GetMapping("/{id}")
    public Congestion getCongestionById(@PathVariable Long id) {
        return congestionService.getById(id).orElse(null);
    }

    @GetMapping("/carte")
    public Map<Long, String> getCongestionsPourCarte() {
        return congestionService.getCongestionsPourCarte();
    }

}