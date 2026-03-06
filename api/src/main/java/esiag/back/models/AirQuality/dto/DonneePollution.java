package esiag.back.models.AirQuality.dto;

import java.time.LocalDateTime;

public interface DonneePollution {

    Float getNo2();
    Float getPm10();
    Float getPm25();
    LocalDateTime getDateMesure();

}
