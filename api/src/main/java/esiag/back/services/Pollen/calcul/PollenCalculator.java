package esiag.back.services.Pollen.calcul;

public class PollenCalculator {
    public static double calculConcentration(double indiceFloraison, String typePollen, String saison, double temp, double hum, double vent) {
        
        double coefType = 1.0;
        String type = typePollen.toLowerCase();
        if (type.equals("bouleau")) {
            coefType = 1.4;
        } else if (type.equals("graminees")) {
            coefType = 1.2;
        } else if (type.equals("olivier")) {
            coefType = 1.0;
        } else {
            coefType = 1.0;
        }

        double coefSaison = 1.0;
        String s = saison.toLowerCase();
        switch (s) {
            case "printemps":
                coefSaison = 1.5;
                break;
            case "ete":
                coefSaison = 1.2;
                break;
            case "automne":
                coefSaison = 0.8;
                break;
            case "hiver":
                coefSaison = 0.4;
                break;
            default:
                coefSaison = 1.0;
        }

        double coefTemp;
        if (temp < 5) {
            coefTemp = 0.5;
        } else if (temp < 15) {
            coefTemp = 1.0;
        } else if (temp < 25) {
            coefTemp = 1.4;
        } else {
            coefTemp = 1.2;
        }

        double coefHum;
        if (hum < 40) {
            coefHum = 1.3;
        } else if (hum < 70) {
            coefHum = 1.0;
        } else {
            coefHum = 0.8;
        }

        double coefVent;
        if (vent < 5) {
            coefVent = 0.8;
        } else if (vent < 20) {
            coefVent = 1.2;
        } else {
            coefVent = 0.9;
        }

 double resultat = indiceFloraison * coefType * coefSaison * coefTemp * coefHum * coefVent;
    if (resultat > 100) {
        return 100.0;
    } else {
        return resultat;
    }
    }

    public static String[] calculLibelleCouleur(double concentration) {
        String[] tab = new String[2];
        if (concentration <= 20) {
            tab[0] = "Faible";
            tab[1] = "#66BB6A";
        } else if (concentration <= 50) {
            tab[0] = "Modéré";
            tab[1] = "#FFFF00";
        } else if (concentration <= 75) {
            tab[0] = "Élevé";
            tab[1] = "#FFA500";
        } else {
            tab[0] = "Très élevé";
            tab[1] = "#FF0000";
        } 
        return tab;
    }
}