package esiag.back.services.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Evenement;
import esiag.back.repositories.TraficRoutier.EvenementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvenementService {

    private final EvenementRepository evenementRepository;

    public EvenementService(EvenementRepository evenementRepository) {
        this.evenementRepository = evenementRepository;
    }

    public List<Evenement> getAll() {
        return evenementRepository.findAll();
    }

    public Optional<Evenement> getById(Long id) {
        return evenementRepository.findById(id);
    }

    public Evenement save(Evenement evenement) {
        return evenementRepository.save(evenement);
    }

    public void delete(Long id) {
        evenementRepository.deleteById(id);
    }
}