package esiag.back.models.TraficRoutier.dto;

import lombok.Data;
import java.util.List;

@Data
public class RouteDTO {

    private Long id;
    private String nom;
    private List<TronconDTO> troncons;
}