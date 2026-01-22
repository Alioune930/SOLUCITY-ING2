package esiag.back.services.AirQuality.calcul;

public class PollutionCalculator {

    public static float normalisationVent(float valeur, float vitesseVent) {
        if(vitesseVent < 5) return valeur * 1.5f;
        else if(vitesseVent < 10) return valeur * 1.3f;
        else if(vitesseVent < 15) return valeur * 1.0f;
        else if(vitesseVent < 25) return valeur * 0.8f;
        else return valeur * 0.6f;
    }

    public static float normalisationHumiditeNO2(float valeur, float humidite) {
        return valeur;
    }

    public static float normalisationHumiditePM(float valeur, float humidite) {
        if(humidite < 30) return valeur * 1.1f;
        else if(humidite < 60) return valeur * 1.0f;
        else if(humidite < 80) return valeur * 1.2f;
        else return valeur * 1.4f;
    }

    public static float normalisationTemperatureNO2(float valeur, float temp) {
        if(temp < 0) return valeur * 1.4f;
        else if(temp < 10) return valeur * 1.3f;
        else if(temp < 25) return valeur * 1.0f;
        else if(temp < 30) return valeur * 1.2f;
        else return valeur * 1.3f;
    }

    public static float normalisationTemperaturePM10(float valeur, float temp) {
        if(temp < 5) return valeur * 1.3f;
        else if(temp < 15) return valeur * 1.2f;
        else if(temp < 25) return valeur * 1.0f;
        else return valeur * 0.9f;
    }

    public static float normalisationTemperaturePM25(float valeur, float temp) {
        if(temp < 5) return valeur * 1.4f;
        else if(temp < 15) return valeur * 1.2f;
        else if(temp < 25) return valeur * 1.0f;
        else return valeur * 0.9f;
    }

    public static float calculeSousIndiceNO2(float indiceNo2Ajuste) {
        return Math.min((indiceNo2Ajuste / MaxPollutionConstants.NO2_MAX) * 100f, 100f);
    }

    public static float calculeSousIndicePM10(float IndicePm10Ajuste) {
        return Math.min((IndicePm10Ajuste / MaxPollutionConstants.PM10_MAX) * 100f, 100f);
    }

    public static float calculeSousIndicePM25(float indicePm25Ajuste) {
        return Math.min((indicePm25Ajuste / MaxPollutionConstants.PM25_MAX) * 100f, 100f);
    }

    public static String[] calculLibelleCouleur(float scoreGlobal) {
        if (scoreGlobal <= 20) {
            return new String[]{"Très bon", "#006400"}; //vert foncé
        } else if (scoreGlobal <= 40) {
            return new String[]{"Bon", "#008000"}; //vert
        } else if (scoreGlobal <= 60) {
            return new String[]{"Moyen", "#FFFF00"}; //jaune
        } else if (scoreGlobal <= 75) {
            return new String[]{"Dégradé", "#FFA500"}; //orange
        } else if (scoreGlobal <= 90) {
            return new String[]{"Mauvais", "#FF0000"}; //rouge
        } else if (scoreGlobal <= 100) {
            return new String[]{"Très mauvais", "#8B00FF"}; //violet
        } else {
            return new String[]{"Extrême", "#8B4513"}; //marron
        }
    }


}
