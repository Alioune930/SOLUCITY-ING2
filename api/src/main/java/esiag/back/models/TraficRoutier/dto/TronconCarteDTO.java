package esiag.back.models.TraficRoutier.dto;

import lombok.Data;

import java.util.List;

@Data
public class TronconCarteDTO {

    private Long id;

    private String nom;

    private double longueur;

    private List<CoordonneeDTO> coordonnees;
}