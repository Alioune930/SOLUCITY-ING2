package esiag.back.services.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Congestion;
import esiag.back.repositories.TraficRoutier.CongestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CongestionService {

    private final CongestionRepository congestionRepository;

    public CongestionService(CongestionRepository congestionRepository) {
        this.congestionRepository = congestionRepository;
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
}