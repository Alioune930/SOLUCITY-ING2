package esiag.back.services.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Troncon;
import esiag.back.repositories.TraficRoutier.TronconRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TronconService {

    private final TronconRepository tronconRepository;

    public TronconService(TronconRepository tronconRepository) {
        this.tronconRepository = tronconRepository;
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
}