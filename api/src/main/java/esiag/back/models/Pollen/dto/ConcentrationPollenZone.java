package esiag.back.models.Pollen.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConcentrationPollenZone {

    private String idCapteur;

    private Long idZone;

    private String ville;

    private String geom;

    private String saison;

    private Double temperature;

    private LocalDateTime timestamp;

    private Double concentration;
}