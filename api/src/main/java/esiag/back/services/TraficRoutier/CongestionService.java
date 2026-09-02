package esiag.back.services.TraficRoutier;

import esiag.back.models.TraficRoutier.dto.CongestionCarteDTO;
import esiag.back.models.TraficRoutier.entity.Capteur;
import esiag.back.models.TraficRoutier.entity.Congestion;
import esiag.back.models.TraficRoutier.entity.CongestionNiveau;
import esiag.back.models.TraficRoutier.entity.MesureTrafic;
import esiag.back.models.TraficRoutier.entity.Troncon;
import esiag.back.repositories.TraficRoutier.CapteurRepository;
import esiag.back.repositories.TraficRoutier.CongestionRepository;
import esiag.back.repositories.TraficRoutier.MesureTraficRepository;
import esiag.back.repositories.TraficRoutier.TronconRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import esiag.back.models.TraficRoutier.dto.CongestionCarteDTO;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CongestionService {

    private final CongestionRepository congestionRepository;
    private final MesureTraficRepository mesureTraficRepository;
    private final TronconRepository tronconRepository;
    private final CapteurRepository capteurRepository;

    public CongestionService(
            CongestionRepository congestionRepository,
            MesureTraficRepository mesureTraficRepository,
            TronconRepository tronconRepository,
            CapteurRepository capteurRepository) {

        this.congestionRepository = congestionRepository;
        this.mesureTraficRepository = mesureTraficRepository;
        this.tronconRepository = tronconRepository;
        this.capteurRepository = capteurRepository;
    }

    public List<Congestion> getAll() {
        return congestionRepository.findAll();
    }

    public Optional<Congestion> getById(Long id) {
        return congestionRepository.findById(id);
    }

    public Congestion save(Congestion congestion) {
        return congestionRepository.save(congestion);
    }

    public void delete(Long id) {
        congestionRepository.deleteById(id);
    }

    public void recalculerPourCapteur(Long capteurId) {

        Optional<MesureTrafic> mesure = mesureTraficRepository
                .findTopByCapteurIdOrderByDateMesureDesc(capteurId);

        Optional<Capteur> capteur = capteurRepository.findById(capteurId);

        if (mesure.isEmpty() || capteur.isEmpty()) {
            return;
        }

        Troncon troncon = capteur.get().getTroncon();

        if (troncon == null) {
            return;
        }

        calculerCongestion(troncon, mesure.get());
    }

    @Transactional
    public void recalculerBatch(List<MesureTrafic> mesures) {

        if (mesures == null || mesures.isEmpty()) {
            return;
        }

        List<Long> capteurIds = mesures.stream()
                .filter(m -> m.getCapteur() != null)
                .map(m -> m.getCapteur().getId())
                .distinct()
                .collect(Collectors.toList());

        // Une seule récupération des capteurs concernés
        List<Capteur> capteurs = capteurRepository.findAllById(capteurIds);

        Map<Long, Capteur> capteursParId = new HashMap<>();

        for (Capteur capteur : capteurs) {
            capteursParId.put(capteur.getId(), capteur);
        }

        Map<Long, Congestion> congestionsParTroncon = new HashMap<>();

        List<Long> tronconIds = capteurs.stream()
                .filter(c -> c.getTroncon() != null)
                .map(c -> c.getTroncon().getId())
                .distinct()
                .collect(Collectors.toList());

        List<Congestion> congestions = congestionRepository.findByTronconIdIn(tronconIds);

        for (Congestion congestion : congestions) {
            congestionsParTroncon.put(
                    congestion.getTroncon().getId(),
                    congestion);
        }

        for (MesureTrafic mesure : mesures) {

            if (mesure.getCapteur() == null) {
                continue;
            }

            Capteur capteur = capteursParId.get(mesure.getCapteur().getId());

            if (capteur == null || capteur.getTroncon() == null) {
                continue;
            }

            Troncon troncon = capteur.getTroncon();

            Congestion congestion = congestionsParTroncon.get(troncon.getId());

            if (congestion == null) {
                congestion = new Congestion();
                congestion.setTroncon(troncon);

                congestionsParTroncon.put(
                        troncon.getId(),
                        congestion);
            }

            appliquerCalcul(congestion, mesure);
        }

        congestionRepository.saveAll(
                congestionsParTroncon.values());
    }

    private void appliquerCalcul(
            Congestion congestion,
            MesureTrafic mesure) {

        double vitesse = mesure.getVitesseMoyenne();
        double occupation = mesure.getTauxOccupation();

        CongestionNiveau niveau;

        if (vitesse >= 40 && occupation < 50) {
            niveau = CongestionNiveau.FLUIDE;

        } else if (vitesse >= 25 && occupation < 75) {
            niveau = CongestionNiveau.MOYEN;

        } else {
            niveau = CongestionNiveau.SATURE;
        }

        congestion.setNiveau(niveau);
        congestion.setVitesseMoyenne(
                mesure.getVitesseMoyenne());
        congestion.setNombreVehicules(
                mesure.getNombreVehicules());
        congestion.setTauxOccupation(
                mesure.getTauxOccupation());
        congestion.setDateCalcul(
                LocalDateTime.now());
    }

    private void calculerCongestion(
            Troncon troncon,
            MesureTrafic mesure) {

        Congestion congestion = congestionRepository
                .findByTronconId(troncon.getId())
                .orElseGet(() -> {
                    Congestion nouvelle = new Congestion();
                    nouvelle.setTroncon(troncon);
                    return nouvelle;
                });

        appliquerCalcul(congestion, mesure);

        congestionRepository.save(congestion);
    }

    public void recalculerToutesLesCongestions() {

        List<Troncon> troncons = tronconRepository.findAll();

        for (Troncon troncon : troncons) {

            if (troncon.getCapteurs() == null ||
                    troncon.getCapteurs().isEmpty()) {
                continue;
            }

            Capteur capteur = troncon.getCapteurs().get(0);

            recalculerPourCapteur(
                    capteur.getId());
        }
    }

    public Map<Long, CongestionCarteDTO> getCongestionsPourCarte() {

        return congestionRepository.findAll()
                .stream()
                .filter(c -> c.getTroncon() != null)
                .collect(Collectors.toMap(
                        c -> c.getTroncon().getId(),
                        c -> {
                            CongestionCarteDTO dto = new CongestionCarteDTO();

                            dto.setNiveau(c.getNiveau().name());
                            dto.setTauxOccupation(c.getTauxOccupation());
                            dto.setVitesseMoyenne(c.getVitesseMoyenne());

                            return dto;
                        }));
    }
}