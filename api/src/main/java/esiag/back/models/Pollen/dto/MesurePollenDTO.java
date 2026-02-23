package esiag.back.models.Pollen.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MesurePollenDTO {

    private String idCapteur;

    private LocalDateTime timestamp;

    private String saison;

    private String typePollen;

    private Double indiceFloraison;

    private Double concentration;

}
