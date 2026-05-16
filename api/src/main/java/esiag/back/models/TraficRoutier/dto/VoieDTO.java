package esiag.back.models.TraficRoutier.dto;

import esiag.back.models.TraficRoutier.entity.Troncon;
import lombok.Data;

@Data
public class VoieDTO {

    private Long id;
    private String nom;
    private Troncon troncon;
}