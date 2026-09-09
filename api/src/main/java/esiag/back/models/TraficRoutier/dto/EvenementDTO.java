package esiag.back.models.TraficRoutier.dto;

import lombok.Data;

@Data
public class EvenementDTO {

    private Long id;
    private String type;
    private String description;
    private Long tronconId;
}