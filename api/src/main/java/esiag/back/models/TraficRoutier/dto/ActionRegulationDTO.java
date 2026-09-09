package esiag.back.models.TraficRoutier.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ActionRegulationDTO {

    private Long id;
    private String typeAction;
    private String description;
    private LocalDateTime dateAction;
    private String statut;
    private Long tronconId;
    private Long voieId;
}