package esiag.back.services.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.ActionRegulation;
import esiag.back.repositories.TraficRoutier.ActionRegulationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActionRegulationService {

    private final ActionRegulationRepository actionRegulationRepository;

    public ActionRegulationService(ActionRegulationRepository actionRegulationRepository) {
        this.actionRegulationRepository = actionRegulationRepository;
    }

    public List<ActionRegulation> getAll() {
        return actionRegulationRepository.findAll();
    }

    public Optional<ActionRegulation> getById(Long id) {
        return actionRegulationRepository.findById(id);
    }

    public ActionRegulation save(ActionRegulation actionRegulation) {
        return actionRegulationRepository.save(actionRegulation);
    }

    public void delete(Long id) {
        actionRegulationRepository.deleteById(id);
    }
}