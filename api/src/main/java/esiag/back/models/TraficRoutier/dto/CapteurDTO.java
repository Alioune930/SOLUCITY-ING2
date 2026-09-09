package esiag.back.models.TraficRoutier.dto;

import lombok.Data;

@Data
public class CapteurDTO {

    private Long id;
    private String reference;
    private String type;
    private Long tronconId;
}