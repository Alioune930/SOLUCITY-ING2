package esiag.back.models.TraficRoutier.dto;

import lombok.Data;

@Data
public class VoieDTO {

    private Long id;
    private String nom;
    private String statut;
    private Long tronconId;
}