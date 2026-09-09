package esiag.back.repositories.TraficRoutier;

import esiag.back.models.TraficRoutier.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}