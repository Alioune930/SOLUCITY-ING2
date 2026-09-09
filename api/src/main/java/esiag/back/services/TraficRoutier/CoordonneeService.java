package esiag.back.services.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Coordonnee;
import esiag.back.repositories.TraficRoutier.CoordonneeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoordonneeService {

    private final CoordonneeRepository coordonneeRepository;

    public CoordonneeService(CoordonneeRepository coordonneeRepository) {
        this.coordonneeRepository = coordonneeRepository;
    }

    public List<Coordonnee> getAll() {
        return coordonneeRepository.findAll();
    }

    public Optional<Coordonnee> getById(Long id) {
        return coordonneeRepository.findById(id);
    }

    public Coordonnee save(Coordonnee coordonnee) {
        return coordonneeRepository.save(coordonnee);
    }

    public void delete(Long id) {
        coordonneeRepository.deleteById(id);
    }
}