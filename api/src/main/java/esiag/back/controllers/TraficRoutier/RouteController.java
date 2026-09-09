package esiag.back.controllers.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Route;
import esiag.back.services.TraficRoutier.RouteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trafic/routes")
@CrossOrigin("*")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public List<Route> getAllRoutes() {
        return routeService.getAll();
    }

    @GetMapping("/{id}")
    public Route getRouteById(@PathVariable Long id) {
        return routeService.getById(id).orElse(null);
    }

    @PostMapping
    public Route createRoute(@RequestBody Route route) {
        return routeService.save(route);
    }

    @DeleteMapping("/{id}")
    public void deleteRoute(@PathVariable Long id) {
        routeService.delete(id);
    }
}