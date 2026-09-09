package esiag.back.controllers.TraficRoutier;

import esiag.back.models.TraficRoutier.dto.ActionRegulationSimulationDTO;
import esiag.back.models.TraficRoutier.entity.ActionRegulation;
import esiag.back.services.TraficRoutier.ActionRegulationService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trafic/regulations")
@CrossOrigin("*")
public class ActionRegulationController {

    private final ActionRegulationService actionRegulationService;

    public ActionRegulationController(
            ActionRegulationService actionRegulationService) {
        this.actionRegulationService = actionRegulationService;
    }

    @PostMapping("/simuler")
    public ActionRegulationSimulationDTO simuler(
            @RequestParam Long tronconId,
            @RequestParam Long voieId,
            @RequestParam String typeAction) {

        return actionRegulationService.simuler(
                tronconId,
                voieId,
                typeAction);
    }

    @PostMapping("/appliquer")
    public String appliquer(
            @RequestParam Long tronconId,
            @RequestParam Long voieId,
            @RequestParam String typeAction) {

        actionRegulationService.appliquer(
                tronconId,
                voieId,
                typeAction);

        return "Régulation appliquée avec succès";
    }

}