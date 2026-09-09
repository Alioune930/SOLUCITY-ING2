package esiag.back.models.TraficRoutier.dto;

import lombok.Data;
import java.util.List;

@Data
public class TronconDTO {

    private Long id;
    private double longueur;
    private String congestionNiveau;
    private List<CoordonneeDTO> coordonnees;
}