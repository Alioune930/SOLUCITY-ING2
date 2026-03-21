package esiag.back.models.TraficRoutier.dto;

import lombok.Data;
import java.util.List;

@Data
public class TronconDTO {

    private String id;
    private double length;
    private String congestion;
    private List<CoordonneeDTO> coordonnees;
    private List<EvenementDTO> evenements;
}