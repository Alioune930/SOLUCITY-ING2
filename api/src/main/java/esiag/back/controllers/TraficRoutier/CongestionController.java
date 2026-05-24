package esiag.back.controllers.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Congestion;
import esiag.back.services.TraficRoutier.CongestionService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public Congestion getCongestionById(@PathVariable Long id) {
        return congestionService.getById(id).orElse(null);
    }

    @PostMapping
    public Congestion createCongestion(@RequestBody Congestion congestion) {
        return congestionService.save(congestion);
    }

    @DeleteMapping("/{id}")
    public void deleteCongestion(@PathVariable Long id) {
        congestionService.delete(id);
    }
}