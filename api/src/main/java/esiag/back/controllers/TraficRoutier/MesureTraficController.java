package esiag.back.controllers.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.MesureTrafic;
import esiag.back.services.TraficRoutier.MesureTraficService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trafic/mesures")
@CrossOrigin("*")
public class MesureTraficController {

    private final MesureTraficService mesureTraficService;

    public MesureTraficController(
            MesureTraficService mesureTraficService) {

        this.mesureTraficService =
                mesureTraficService;
    }

    @GetMapping
    public List<MesureTrafic> getAllMesures() {
        return mesureTraficService.getAll();
    }

    @GetMapping("/{id}")
    public MesureTrafic getMesureById(
            @PathVariable Long id) {

        return mesureTraficService
                .getById(id)
                .orElse(null);
    }

    @PostMapping
    public MesureTrafic createMesure(
            @RequestBody MesureTrafic mesureTrafic) {

        return mesureTraficService.save(mesureTrafic);
    }

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createMesures(
            @RequestBody List<MesureTrafic> mesures) {

        mesureTraficService.saveAll(mesures);
    }

    @DeleteMapping("/{id}")
    public void deleteMesure(
            @PathVariable Long id) {

        mesureTraficService.delete(id);
    }
}