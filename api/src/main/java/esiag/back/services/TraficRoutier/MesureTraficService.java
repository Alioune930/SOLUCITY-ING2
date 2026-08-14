package esiag.back.services.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Capteur;
import esiag.back.models.TraficRoutier.entity.MesureTrafic;
import esiag.back.repositories.TraficRoutier.CapteurRepository;
import esiag.back.repositories.TraficRoutier.MesureTraficRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MesureTraficService {

    private final MesureTraficRepository mesureTraficRepository;
    private final CapteurRepository capteurRepository;
    private final CongestionService congestionService;

    public MesureTraficService(
            MesureTraficRepository mesureTraficRepository,
            CapteurRepository capteurRepository,
            CongestionService congestionService) {

        this.mesureTraficRepository = mesureTraficRepository;
        this.capteurRepository = capteurRepository;
        this.congestionService = congestionService;
    }

    public List<MesureTrafic> getAll() {
        return mesureTraficRepository.findAll();
    }

    public Optional<MesureTrafic> getById(Long id) {
        return mesureTraficRepository.findById(id);
    }

    public MesureTrafic save(MesureTrafic mesureTrafic) {
        return mesureTraficRepository.save(mesureTrafic);
    }

    @Transactional
    public List<MesureTrafic> saveAll(List<MesureTrafic> nouvellesMesures) {

        if (nouvellesMesures == null || nouvellesMesures.isEmpty()) {
            return nouvellesMesures;
        }

        List<Long> capteurIds = nouvellesMesures.stream()
                .filter(m -> m.getCapteur() != null)
                .map(m -> m.getCapteur().getId())
                .collect(Collectors.toList());

        // Récupération des mesures existantes en une seule requête
        List<MesureTrafic> mesuresExistantes =
                mesureTraficRepository
                        .findByCapteurIdInOrderByDateMesureDesc(capteurIds);

        Map<Long, MesureTrafic> mesuresParCapteur = new HashMap<>();

        for (MesureTrafic mesure : mesuresExistantes) {
            Long capteurId = mesure.getCapteur().getId();

            // La requête est triée par date DESC :
            // on conserve donc uniquement la plus récente.
            mesuresParCapteur.putIfAbsent(capteurId, mesure);
        }

        for (MesureTrafic nouvelle : nouvellesMesures) {

            if (nouvelle.getCapteur() == null) {
                continue;
            }

            Long capteurId = nouvelle.getCapteur().getId();

            MesureTrafic existante = mesuresParCapteur.get(capteurId);

            if (existante != null) {

                existante.setVitesseMoyenne(
                        nouvelle.getVitesseMoyenne()
                );

                existante.setNombreVehicules(
                        nouvelle.getNombreVehicules()
                );

                existante.setTauxOccupation(
                        nouvelle.getTauxOccupation()
                );

                existante.setDateMesure(
                        nouvelle.getDateMesure()
                );

            } else {
                // Cas exceptionnel : aucun historique pour ce capteur.
                mesuresParCapteur.put(capteurId, nouvelle);
            }
        }

        List<MesureTrafic> mesuresAEnregistrer =
                mesuresParCapteur.values().stream().collect(Collectors.toList());

        List<MesureTrafic> mesuresSauvegardees =
                mesureTraficRepository.saveAll(mesuresAEnregistrer);

        // Un seul traitement batch des congestions
        congestionService.recalculerBatch(mesuresSauvegardees);

        return mesuresSauvegardees;
    }

    public void delete(Long id) {
        mesureTraficRepository.deleteById(id);
    }
}