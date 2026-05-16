package esiag.back.models.TraficRoutier.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MesureTraficDTO {

    private Long id;
    private double vitesseMoyenne;
    private int nombreVehicules;
    private double tauxOccupation;
    private LocalDateTime dateMesure;
    private Long capteurId;
}