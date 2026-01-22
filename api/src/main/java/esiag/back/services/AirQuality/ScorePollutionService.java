package esiag.back.services.AirQuality;

import esiag.back.models.AirQuality.dto.*;
import esiag.back.repositories.AirQuality.*;
import esiag.back.services.AirQuality.calcul.PollutionCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScorePollutionService {

    @Autowired
    private ZoneVilleRepository zoneVilleRepository;
    @Autowired
    private CapteurPollutionRepository capteurPollutionRepository;
    @Autowired
    private CapteurMeteoRepository capteurMeteoRepository;
    @Autowired
    private MesurePollutionRepository mesurePollutionRepository;
    @Autowired
    private MesureMeteoRepository mesureMeteoRepository;

    public List<ScorePollutionZone> calculeZonePollution(){
        List<ScorePollutionZone> scorePollutionZones = new ArrayList<>();
        List<Long> zonesId = zoneVilleRepository.findAllZonesId();

        for (Long zoneId : zonesId) {

            try{
                CapteurPollutionLight cp = capteurPollutionRepository.findCapteurPollutionInZone(zoneId).get(0);
                CapteurMeteoLight cm = capteurMeteoRepository.findNearestMeteoToZone(zoneId).get(0);

                DonneePollution dp = mesurePollutionRepository.findByIdCapteurPollution(cp.getIdCapteurPollution()).get(0);
                DonneeMeteo dm = mesureMeteoRepository.findByIdCapteurMeteo(cm.getIdCapteurMeteo()).get(0);

                float no2Aj = PollutionCalculator.normalisationVent(dp.getNo2(),dm.getVitesseVent());
                no2Aj = PollutionCalculator.normalisationHumiditeNO2(no2Aj,dm.getHumidite());
                no2Aj = PollutionCalculator.normalisationTemperatureNO2(no2Aj,dm.getTemperature());

                float pm10Aj = PollutionCalculator.normalisationVent(dp.getPm10(),dm.getVitesseVent());
                pm10Aj = PollutionCalculator.normalisationHumiditePM(pm10Aj,dm.getHumidite());
                pm10Aj = PollutionCalculator.normalisationTemperaturePM10(pm10Aj,dm.getTemperature());

                float pm25Aj = PollutionCalculator.normalisationVent(dp.getPm25(),dm.getVitesseVent());
                pm25Aj = PollutionCalculator.normalisationHumiditePM(pm25Aj,dm.getHumidite());
                pm25Aj = PollutionCalculator.normalisationTemperaturePM25(pm25Aj,dm.getTemperature());

                float sousIndiceNo2 = PollutionCalculator.calculeSousIndiceNO2(no2Aj);
                float sousIndicepm10Aj = PollutionCalculator.calculeSousIndicePM10(pm10Aj);
                float sousIndicepm25Aj = PollutionCalculator.calculeSousIndicePM25(pm25Aj);

                float score_global = Math.max(no2Aj,Math.max(pm10Aj,pm25Aj));

                ScorePollutionZone spz = new ScorePollutionZone();

                spz.setZoneId(zoneId);
                spz.setIndiceNo2Ajuste(no2Aj);
                spz.setIndicePm10Ajuste(pm10Aj);
                spz.setIndicePm25Ajuste(pm25Aj);
                spz.setSousIndiceNo2(sousIndiceNo2);
                spz.setSousIndicePm10(sousIndicepm10Aj);
                spz.setSousIndicePm25(sousIndicepm25Aj);
                spz.setScoreGlobalPollution(score_global);
                spz.setDateMesure(LocalDateTime.now());

                scorePollutionZones.add(spz);


            } catch (Exception e) {
                System.err.println("Erreur pour la zone " + zoneId + ": " + e.getMessage());
            }

        }

        return scorePollutionZones;

    }




}
