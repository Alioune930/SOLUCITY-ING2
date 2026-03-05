package esiag.back.services.Pollen;

import esiag.back.models.Pollen.dto.ConcentrationPollenZone;
import esiag.back.models.Pollen.dto.DonneePollenLight;
import esiag.back.repositories.Pollen.CapteurPollenRepository;
import esiag.back.repositories.Pollen.MesurePollenRepository;
import esiag.back.repositories.AirQuality.CapteurMeteoRepository;
import esiag.back.repositories.AirQuality.MesureMeteoRepository;
import esiag.back.repositories.AirQuality.ZoneVilleRepository;
import esiag.back.services.Pollen.calcul.PollenCalculator;
import esiag.back.models.AirQuality.dto.DonneeMeteo;
import esiag.back.models.Pollen.dto.CapteurPollenLight;
import esiag.back.models.AirQuality.dto.CapteurMeteoLight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScorePollenService {

    @Autowired
    private ZoneVilleRepository zoneVilleRepository;
    @Autowired
    private CapteurPollenRepository capteurPollenRepository;
    @Autowired
    private CapteurMeteoRepository capteurMeteoRepository;
    @Autowired
    private MesurePollenRepository mesurePollenRepository;
    @Autowired
    private MesureMeteoRepository mesureMeteoRepository;

    public List<ConcentrationPollenZone> calculeConcentrationZones() {

        List<ConcentrationPollenZone> resultats = new ArrayList<ConcentrationPollenZone>();
        
        List<Object[]> zonesData = zoneVilleRepository.findAllZonesGeoJson();
        Map<Long, String> mapGeom = new HashMap<>();
        for (Object[] obj : zonesData) {
            mapGeom.put(((Number) obj[0]).longValue(), (String) obj[2]);
        }

        List<Long> listeIds = zoneVilleRepository.findAllZonesId();

        for (int i = 0; i < listeIds.size(); i++) {
            Long idZone = listeIds.get(i);

            try {
                List<CapteurPollenLight> capteurs = capteurPollenRepository.findCapteurPollenInZone(idZone);
                if (capteurs.size() > 0) {
                    CapteurPollenLight leCapteur = capteurs.get(0);

                    double t = 20.0, h = 50.0, v = 10.0;

                    List<CapteurMeteoLight> listeMeteo = capteurMeteoRepository.findNearestMeteoToZone(idZone);
                    if (listeMeteo.size() > 0) {
                        List<DonneeMeteo> mesuresMeteo = mesureMeteoRepository.findByIdCapteurMeteo(listeMeteo.get(0).getIdCapteurMeteo());
                        if (mesuresMeteo.size() > 0) {
                            DonneeMeteo dm = mesuresMeteo.get(mesuresMeteo.size() - 1);
                            t = dm.getTemperature();
                            h = dm.getHumidite();
                            v = dm.getVitesseVent();
                        }
                    }

DonneePollenLight derniereMesure = mesurePollenRepository.findLatestByCapteur(leCapteur.getIdCapteur());

                    if (derniereMesure != null) {
                        double score = PollenCalculator.calculConcentration(
                            derniereMesure.getIndiceFloraison(),
                            derniereMesure.getTypePollen(),
                            derniereMesure.getSaison(),
                            t, h, v
                        );

                        ConcentrationPollenZone cpz = new ConcentrationPollenZone();
                        cpz.setIdZone(idZone);
                        cpz.setGeom(mapGeom.get(idZone));
                        cpz.setIdCapteur(leCapteur.getIdCapteur());
                        cpz.setVille(leCapteur.getVille());
                        cpz.setConcentration(score);
                        cpz.setTimestamp(derniereMesure.getTimestamp());
                        cpz.setSaison(derniereMesure.getSaison()); 
                        cpz.setTemperature(t);
                        resultats.add(cpz);
                        System.out.println("Zone calculee : " + leCapteur.getVille());
                    }
                }
            } catch (Exception e) {
                System.out.println("Erreur zone : " + idZone);
            }
        }
        return resultats;
    }
}