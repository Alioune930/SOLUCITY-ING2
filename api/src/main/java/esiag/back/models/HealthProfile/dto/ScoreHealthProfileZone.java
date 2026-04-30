package esiag.back.models.HealthProfile.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScoreHealthProfileZone {

    private Long idZone;
    private String geom;

    private float scorePollutionAjuste;
    private double scorePollenAjuste;
    private double scoreRisqueGlobal;

    private LocalDateTime dateMesure;
}
