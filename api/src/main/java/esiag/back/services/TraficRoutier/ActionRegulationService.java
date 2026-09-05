package esiag.back.services.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.ActionRegulation;
import esiag.back.repositories.TraficRoutier.ActionRegulationRepository;
import org.springframework.stereotype.Service;
import esiag.back.models.TraficRoutier.dto.ActionRegulationSimulationDTO;
import esiag.back.models.TraficRoutier.entity.Congestion;
import esiag.back.models.TraficRoutier.entity.Voie;
import esiag.back.repositories.TraficRoutier.CongestionRepository;
import esiag.back.repositories.TraficRoutier.VoieRepository;

import java.util.List;

import java.util.Optional;

@Service
public class ActionRegulationService {

    private final ActionRegulationRepository actionRegulationRepository;
    private final CongestionRepository congestionRepository;
    private final VoieRepository voieRepository;

    public ActionRegulationService(
            ActionRegulationRepository actionRegulationRepository,
            CongestionRepository congestionRepository,
            VoieRepository voieRepository) {

        this.actionRegulationRepository = actionRegulationRepository;
        this.congestionRepository = congestionRepository;
        this.voieRepository = voieRepository;
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

    public ActionRegulationSimulationDTO simuler(
            Long tronconId,
            Long voieId,
            String typeAction) {

        Congestion congestion = congestionRepository
                .findByTronconId(tronconId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Aucune congestion trouvée pour ce tronçon"));

        List<Voie> voies = voieRepository.findByTronconId(tronconId);

        Voie voie = voieRepository.findById(voieId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Voie introuvable"));

        if (!voie.getTroncon().getId().equals(tronconId)) {
            throw new IllegalArgumentException(
                    "La voie ne correspond pas au tronçon");
        }

        int voiesOuvertesAvant = (int) voies.stream()
                .filter(v -> "OUVERTE".equalsIgnoreCase(v.getStatut()))
                .count();

        int voiesOuvertesApres = voiesOuvertesAvant;

        if ("OUVRIR_VOIE".equalsIgnoreCase(typeAction)) {

            if (!"FERMEE".equalsIgnoreCase(voie.getStatut())) {
                throw new IllegalArgumentException(
                        "La voie sélectionnée est déjà ouverte");
            }

            voiesOuvertesApres++;

        } else if ("FERMER_VOIE".equalsIgnoreCase(typeAction)) {

            if (!"OUVERTE".equalsIgnoreCase(voie.getStatut())) {
                throw new IllegalArgumentException(
                        "La voie sélectionnée est déjà fermée");
            }

            if (voiesOuvertesAvant <= 1) {
                throw new IllegalArgumentException(
                        "Impossible de fermer la dernière voie ouverte");
            }

            voiesOuvertesApres--;

        } else {
            throw new IllegalArgumentException(
                    "Type d'action non pris en charge pour cette simulation");
        }

        double occupationAvant = congestion.getTauxOccupation();

        double occupationApres;

        if ("OUVRIR_VOIE".equalsIgnoreCase(typeAction)) {
            occupationApres = occupationAvant * 0.80;
        } else {
            occupationApres = occupationAvant * 1.20;
        }

        occupationApres = Math.min(100, Math.max(0, occupationApres));

        String niveauEstimeApres;

        if (occupationApres < 50
                && congestion.getVitesseMoyenne() >= 40) {
            niveauEstimeApres = "FLUIDE";
        } else if (occupationApres < 75
                && congestion.getVitesseMoyenne() >= 25) {
            niveauEstimeApres = "MOYEN";
        } else {
            niveauEstimeApres = "SATURE";
        }

        ActionRegulationSimulationDTO dto = new ActionRegulationSimulationDTO();

        dto.setTronconId(tronconId);
        dto.setVoieId(voieId);
        dto.setNiveauActuel(congestion.getNiveau().name());
        dto.setTypeAction(typeAction.toUpperCase());

        dto.setDescription(
                "Simulation de l'action "
                        + typeAction.toUpperCase()
                        + " sur " + voie.getNom());

        dto.setVoiesOuvertesAvant(voiesOuvertesAvant);
        dto.setVoiesOuvertesApres(voiesOuvertesApres);

        dto.setOccupationAvant(occupationAvant);
        dto.setOccupationApres(occupationApres);

        dto.setNiveauEstimeApres(niveauEstimeApres);

        return dto;
    }

    public ActionRegulation appliquer(
        Long tronconId,
        Long voieId,
        String typeAction) {

    Voie voie = voieRepository.findById(voieId)
            .orElseThrow(() -> new IllegalArgumentException(
                    "Voie introuvable"));

    if (voie.getTroncon() == null
            || !voie.getTroncon().getId().equals(tronconId)) {
        throw new IllegalArgumentException(
                "La voie ne correspond pas au tronçon");
    }

    if ("OUVRIR_VOIE".equalsIgnoreCase(typeAction)) {

        if ("OUVERTE".equalsIgnoreCase(voie.getStatut())) {
            throw new IllegalArgumentException(
                    "La voie sélectionnée est déjà ouverte");
        }

        voie.setStatut("OUVERTE");

    } else if ("FERMER_VOIE".equalsIgnoreCase(typeAction)) {

        if ("FERMEE".equalsIgnoreCase(voie.getStatut())) {
            throw new IllegalArgumentException(
                    "La voie sélectionnée est déjà fermée");
        }

        List<Voie> voies = voieRepository.findByTronconId(tronconId);

        long voiesOuvertes = voies.stream()
                .filter(v -> "OUVERTE".equalsIgnoreCase(v.getStatut()))
                .count();

        if (voiesOuvertes <= 1) {
            throw new IllegalArgumentException(
                    "Impossible de fermer la dernière voie ouverte");
        }

        voie.setStatut("FERMEE");

    } else {
        throw new IllegalArgumentException(
                "Type d'action non pris en charge");
    }

    voieRepository.save(voie);

    ActionRegulation action = new ActionRegulation();
    action.setTypeAction(typeAction.toUpperCase());
    action.setDescription(
            "Application de l'action "
                    + typeAction.toUpperCase()
                    + " sur " + voie.getNom());
    action.setDateAction(java.time.LocalDateTime.now());
    action.setStatut("APPLIQUEE");
    action.setTroncon(voie.getTroncon());

    return actionRegulationRepository.save(action);
}
}