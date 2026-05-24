package esiag.back.services.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Route;
import esiag.back.repositories.TraficRoutier.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public List<Route> getAll() {
        return routeRepository.findAll();
    }

    public Optional<Route> getById(Long id) {
        return routeRepository.findById(id);
    }

    public Route save(Route route) {
        return routeRepository.save(route);
    }

    public void delete(Long id) {
        routeRepository.deleteById(id);
    }
}