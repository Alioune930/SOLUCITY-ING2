package esiag.back.models.TraficRoutier.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CongestionDTO {

    private Long id;
    private String niveau;
    private double vitesseMoyenne;
    private int nombreVehicules;
    private double tauxOccupation;
    private LocalDateTime dateCalcul;
    private Long tronconId;
}
