package esiag.back.services.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.MesureTrafic;
import esiag.back.repositories.TraficRoutier.MesureTraficRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MesureTraficService {

    private final MesureTraficRepository mesureTraficRepository;

    public MesureTraficService(MesureTraficRepository mesureTraficRepository) {
        this.mesureTraficRepository = mesureTraficRepository;
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

    public void delete(Long id) {
        mesureTraficRepository.deleteById(id);
    }
}