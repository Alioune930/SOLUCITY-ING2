package esiag.back.services.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Voie;
import esiag.back.repositories.TraficRoutier.VoieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoieService {

    private final VoieRepository voieRepository;

    public VoieService(VoieRepository voieRepository) {
        this.voieRepository = voieRepository;
    }

    public List<Voie> getAll() {
        return voieRepository.findAll();
    }

    public Optional<Voie> getById(Long id) {
        return voieRepository.findById(id);
    }

    public Voie save(Voie voie) {
        return voieRepository.save(voie);
    }

    public void delete(Long id) {
        voieRepository.deleteById(id);
    }
}