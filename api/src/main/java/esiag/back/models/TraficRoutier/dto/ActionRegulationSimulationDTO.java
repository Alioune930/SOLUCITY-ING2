package esiag.back.models.TraficRoutier.dto;

import lombok.Data;

@Data
public class ActionRegulationSimulationDTO {

    private Long tronconId;
    private Long voieId;

    private String niveauActuel;
    private String typeAction;

    private String description;

    private int voiesOuvertesAvant;
    private int voiesOuvertesApres;

    private double occupationAvant;
    private double occupationApres;

    private String niveauEstimeApres;
}