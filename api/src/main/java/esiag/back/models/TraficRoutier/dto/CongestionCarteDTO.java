package esiag.back.models.TraficRoutier.dto;

import lombok.Data;

@Data
public class CongestionCarteDTO {

    private String niveau;
    private double tauxOccupation;
    private double vitesseMoyenne;
}