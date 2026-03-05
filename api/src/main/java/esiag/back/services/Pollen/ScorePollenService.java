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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

        List<ConcentrationPollenZone> resultatsListe = new ArrayList<ConcentrationPollenZone>();
        
        List<Long> idsZones = zoneVilleRepository.findAllZonesId();

        for (int i = 0; i < idsZones.size(); i++) {
            Long idZ = idsZones.get(i);

            try {
                CapteurPollenLight cp = capteurPollenRepository.findCapteurPollenInZone(idZ).get(0);

                CapteurMeteoLight cm = capteurMeteoRepository.findNearestMeteoToZone(idZ).get(0);
                DonneeMeteo dm = mesureMeteoRepository.findByIdCapteurMeteo(cm.getIdCapteurMeteo()).get(0);

                List<DonneePollenLight> listeMesures = mesurePollenRepository.findByCapteur(cp.getIdCapteur());

                double total = 0.0;
                
                for (DonneePollenLight m : listeMesures) {
                    double calcul = PollenCalculator.calculConcentration(
                            m.getIndiceFloraison(),
                            m.getTypePollen(),
                            m.getSaison(),
                            dm.getTemperature(),  
                            dm.getHumidite(),      
                            dm.getVitesseVent()   
                    );
                    total = total + calcul;
                }

                double moyenneZone = 0;
                if (listeMesures.size() > 0) {
                    moyenneZone = total / listeMesures.size();
                }

               ConcentrationPollenZone res = new ConcentrationPollenZone();
                res.setIdCapteur(cp.getIdCapteur());
                res.setVille(cp.getVille());
                res.setGeom(cp.getGeom());
                res.setConcentration(moyenneZone);

                if (!listeMesures.isEmpty()) {
                    int dernierIndex = listeMesures.size() - 1;
                    LocalDateTime dateDerniereMesure = listeMesures.get(dernierIndex).getTimestamp();
                    
                    res.setTimestamp(dateDerniereMesure);
                } else {
                    res.setTimestamp(LocalDateTime.now());
                };
                
                System.out.println(" Calcul fini pour " + cp.getVille() + " (" + moyenneZone + ")");
                resultatsListe.add(res);

            } catch (Exception e) {
                System.out.println("Erreur zone " + idZ + " : " + e.getMessage());
            }
        }

        return resultatsListe;
    }
}