package esiag.back.controllers.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Troncon;
import esiag.back.services.TraficRoutier.TronconService;
import org.springframework.web.bind.annotation.*;
import esiag.back.models.TraficRoutier.dto.TronconCarteDTO;
import java.util.List;

@RestController
@RequestMapping("/api/trafic/troncons")
@CrossOrigin("*")
public class TronconController {

    private final TronconService tronconService;

    public TronconController(TronconService tronconService) {
        this.tronconService = tronconService;
    }

    @GetMapping
    public List<Troncon> getAllTroncons() {
        return tronconService.getAll();
    }

    @GetMapping("/{id}")
    public Troncon getTronconById(@PathVariable Long id) {
        return tronconService.getById(id).orElse(null);
    }

    @PostMapping
    public Troncon createTroncon(@RequestBody Troncon troncon) {
        return tronconService.save(troncon);
    }

    @DeleteMapping("/{id}")
    public void deleteTroncon(@PathVariable Long id) {
        tronconService.delete(id);
    }

    @GetMapping("/carte")
        public List<TronconCarteDTO> getTronconsPourCarte() {
        return tronconService.getTronconsPourCarte();
    }
    
}