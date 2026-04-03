package esiag.back.services.AirQuality.calcul;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PollutionCalculatorTest {

    @Test
    void normalisationVent() {
        assertEquals(15f, PollutionCalculator.normalisationVent(10f, 0f));//vitesseVent < 5
        assertEquals(13f, PollutionCalculator.normalisationVent(10f, 5f));//vitesseVent < 10
        assertEquals(10f, PollutionCalculator.normalisationVent(10f, 12f));//vitesseVent < 15
        assertEquals(8f, PollutionCalculator.normalisationVent(10f, 20f));//vitesseVent < 25
        assertEquals(6f, PollutionCalculator.normalisationVent(10f, 30f));//vitesseVent >= 25
    }

    @Test
    void normalisationHumiditePM() {
        assertEquals(11f, PollutionCalculator.normalisationHumiditePM(10f, 20f));//humidité < 30
        assertEquals(10f, PollutionCalculator.normalisationHumiditePM(10f, 50f));//humidité < 60
        assertEquals(12f, PollutionCalculator.normalisationHumiditePM(10f, 70f));//humidité < 80
        assertEquals(14f, PollutionCalculator.normalisationHumiditePM(10f, 90f));//humidité >= 80
    }



    @Test
    void calculeSousIndiceNO2() {
        assertEquals(50f, PollutionCalculator.calculeSousIndiceNO2(MaxPollutionConstants.NO2_MAX / 2f));
        assertEquals(100f, PollutionCalculator.calculeSousIndiceNO2(MaxPollutionConstants.NO2_MAX));
        assertEquals(0f, PollutionCalculator.calculeSousIndiceNO2(0f));
        assertEquals(100f, PollutionCalculator.calculeSousIndiceNO2(MaxPollutionConstants.NO2_MAX * 2));
    }


    @Test
    void calculLibelleCouleur() {
        assertArrayEquals(new String[]{"Très bon", "#006400"}, PollutionCalculator.calculLibelleCouleur(10f));
        assertArrayEquals(new String[]{"Moyen", "#FFFF00"}, PollutionCalculator.calculLibelleCouleur(50f));
        assertArrayEquals(new String[]{"Extrême", "#8B4513"}, PollutionCalculator.calculLibelleCouleur(150f));
    }
}