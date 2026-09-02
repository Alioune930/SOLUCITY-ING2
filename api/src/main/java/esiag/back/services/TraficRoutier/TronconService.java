package esiag.back.services.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Troncon;
import esiag.back.repositories.TraficRoutier.TronconRepository;
import org.springframework.stereotype.Service;
import esiag.back.models.TraficRoutier.dto.CoordonneeDTO;
import esiag.back.models.TraficRoutier.dto.TronconCarteDTO;
import esiag.back.models.TraficRoutier.entity.Coordonnee;
import esiag.back.repositories.TraficRoutier.CongestionRepository;
import esiag.back.models.TraficRoutier.entity.Congestion;
import esiag.back.models.TraficRoutier.entity.CongestionNiveau;

import java.util.List;
import java.util.Optional;

@Service
public class TronconService {

    private final TronconRepository tronconRepository;
    private final CongestionRepository congestionRepository;

    public TronconService(
            TronconRepository tronconRepository,
            CongestionRepository congestionRepository) {

        this.tronconRepository = tronconRepository;
        this.congestionRepository = congestionRepository;
    }

    public List<Troncon> getAll() {
        return tronconRepository.findAll();
    }

    public Optional<Troncon> getById(Long id) {
        return tronconRepository.findById(id);
    }

    public Troncon save(Troncon troncon) {
        return tronconRepository.save(troncon);
    }

    public void delete(Long id) {
        tronconRepository.deleteById(id);
    }

    public List<TronconCarteDTO> getTronconsPourCarte() {

        List<Troncon> troncons = tronconRepository.findAll();

        return troncons.stream().map(troncon -> {

            TronconCarteDTO dto = new TronconCarteDTO();

            dto.setId(troncon.getId());
            dto.setNom(
                    troncon.getRoute() != null
                            ? troncon.getRoute().getNom()
                            : "Route inconnue");
            dto.setLongueur(troncon.getLongueur());

            List<CoordonneeDTO> coordonneesDTO = troncon.getCoordonnees().stream().map(coordonnee -> {

                CoordonneeDTO coordDTO = new CoordonneeDTO();

                coordDTO.setLatitude(coordonnee.getLatitude());
                coordDTO.setLongitude(coordonnee.getLongitude());

                return coordDTO;

            }).toList();

            dto.setCoordonnees(coordonneesDTO);

            return dto;

        }).toList();
    }

    public List<TronconCarteDTO> getTronconsProblemes() {

        List<Congestion> congestions = congestionRepository.findAll();

        return congestions.stream()
                .filter(c -> c.getTroncon() != null)
                .filter(c -> c.getNiveau() == CongestionNiveau.MOYEN
                        || c.getNiveau() == CongestionNiveau.SATURE)
                .map(c -> {
                    Troncon troncon = c.getTroncon();

                    TronconCarteDTO dto = new TronconCarteDTO();

                    dto.setId(troncon.getId());

                    String nom;
                    if (troncon.getRoute() != null
                            && troncon.getRoute().getNom() != null
                            && !troncon.getRoute().getNom().equalsIgnoreCase("Voie unclassified")) {
                        nom = troncon.getRoute().getNom();
                    } else {
                        nom = "Route inconnue";
                    }

                    dto.setNom(nom);
                    dto.setLongueur(troncon.getLongueur());

                    List<CoordonneeDTO> coordonneesDTO = troncon.getCoordonnees().stream().map(coordonnee -> {

                        CoordonneeDTO coordDTO = new CoordonneeDTO();
                        coordDTO.setLatitude(coordonnee.getLatitude());
                        coordDTO.setLongitude(coordonnee.getLongitude());

                        return coordDTO;
                    }).toList();

                    dto.setCoordonnees(coordonneesDTO);

                    return dto;
                })
                .toList();
    }
}