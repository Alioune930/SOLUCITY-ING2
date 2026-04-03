package esiag.back.services.HealthProfile.calcul;

import esiag.back.models.HealthProfile.HealthProfil;

public class HealthProfileCalculator {

    private boolean PollutionTrigger(HealthProfil profil) {
        return profil.getDeclencheur() != null &&
                (profil.getDeclencheur().equalsIgnoreCase(HealthProfileConstants.POLLUTION)
                        || profil.getDeclencheur().equalsIgnoreCase(HealthProfileConstants.POLLUTION_POLLEN));
    }

    private boolean PollenTrigger(HealthProfil profil) {
        return profil.getDeclencheur() != null &&
                (profil.getDeclencheur().equalsIgnoreCase(HealthProfileConstants.POLLEN)
                        || profil.getDeclencheur().equalsIgnoreCase(HealthProfileConstants.POLLUTION_POLLEN));
    }

    public float HealthProfilScorePollution(float score, HealthProfil profil) {

        float facteur = 1.0f;

        if (profil.isAsthme() && PollutionTrigger(profil)) {
            facteur += 0.2f;
        }

        if (profil.getSensibilitePollution() != null) {
            switch (profil.getSensibilitePollution().toLowerCase()) {
                case "leger": facteur += 0.1f; break;
                case "moyenne": facteur += 0.2f; break;
                case "severe": facteur += 0.4f; break;
            }
        }

        if (PollutionTrigger(profil)) {
            facteur += 0.3f;
        }

        return Math.min(score * facteur, 100f);
    }

    public double HealthProfilScorePollen(double score, HealthProfil profil) {

        float facteur = 1.0f;

        if (profil.isAsthme() && PollenTrigger(profil)) {
            facteur += 0.2f;
        }

        if (profil.getSensibilitePollen() != null) {
            switch (profil.getSensibilitePollen().toLowerCase()) {
                case "faible": facteur += 0.1f; break;
                case "moderee": facteur += 0.3f; break;
                case "forte": facteur += 0.5f; break;
            }
        }

        if (PollenTrigger(profil)) {
            facteur += 0.3f;
        }

        return Math.min(score * facteur, 100);
    }

}
