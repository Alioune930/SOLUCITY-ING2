package esiag.back.models.AirQuality.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScorePollutionZone {

    private Long zoneId;
    private float IndiceNo2Ajuste;
    private float IndicePm10Ajuste;
    private float indicePm25Ajuste;
    private float sousIndiceNo2;
    private float sousIndicePm10;
    private float sousIndicePm25;
    private float scoreGlobalPollution;
    private LocalDateTime dateMesure;


}
