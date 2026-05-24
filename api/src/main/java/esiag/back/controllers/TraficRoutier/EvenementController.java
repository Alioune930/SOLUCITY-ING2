package esiag.back.controllers.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Evenement;
import esiag.back.services.TraficRoutier.EvenementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trafic/evenements")
@CrossOrigin("*")
public class EvenementController {

    private final EvenementService evenementService;

    public EvenementController(EvenementService evenementService) {
        this.evenementService = evenementService;
    }

    @GetMapping
    public List<Evenement> getAllEvenements() {
        return evenementService.getAll();
    }

    @GetMapping("/{id}")
    public Evenement getEvenementById(@PathVariable Long id) {
        return evenementService.getById(id).orElse(null);
    }

    @PostMapping
    public Evenement createEvenement(@RequestBody Evenement evenement) {
        return evenementService.save(evenement);
    }

    @DeleteMapping("/{id}")
    public void deleteEvenement(@PathVariable Long id) {
        evenementService.delete(id);
    }
}