package esiag.back.controllers.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Voie;
import esiag.back.services.TraficRoutier.VoieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trafic/voies")
@CrossOrigin("*")
public class VoieController {

    private final VoieService voieService;

    public VoieController(VoieService voieService) {
        this.voieService = voieService;
    }

    @GetMapping
    public List<Voie> getAllVoies() {
        return voieService.getAll();
    }

    @GetMapping("/{id}")
    public Voie getVoieById(@PathVariable Long id) {
        return voieService.getById(id).orElse(null);
    }

    @PostMapping
    public Voie createVoie(@RequestBody Voie voie) {
        return voieService.save(voie);
    }

    @DeleteMapping("/{id}")
    public void deleteVoie(@PathVariable Long id) {
        voieService.delete(id);
    }

    @GetMapping("/troncon/{tronconId}")
    public List<Voie> getVoiesByTroncon(@PathVariable Long tronconId) {
        return voieService.getByTronconId(tronconId);
    }
}