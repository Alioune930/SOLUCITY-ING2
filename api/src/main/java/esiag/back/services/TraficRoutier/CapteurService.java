package esiag.back.services.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Capteur;
import esiag.back.repositories.TraficRoutier.CapteurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CapteurService {

    private final CapteurRepository capteurRepository;

    public CapteurService(CapteurRepository capteurRepository) {
        this.capteurRepository = capteurRepository;
    }

    public List<Capteur> getAll() {
        return capteurRepository.findAll();
    }

    public Optional<Capteur> getById(Long id) {
        return capteurRepository.findById(id);
    }

    public Capteur save(Capteur capteur) {
        return capteurRepository.save(capteur);
    }

    public void delete(Long id) {
        capteurRepository.deleteById(id);
    }
}