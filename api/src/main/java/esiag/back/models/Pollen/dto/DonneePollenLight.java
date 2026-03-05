package esiag.back.models.Pollen.dto;

import java.time.LocalDateTime;

public interface DonneePollenLight {

    String getTypePollen();

    Double getIndiceFloraison();

    String getSaison();

    LocalDateTime getTimestamp();
}