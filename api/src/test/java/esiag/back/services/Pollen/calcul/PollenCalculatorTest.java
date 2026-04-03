package esiag.back.services.Pollen.calcul;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class PollenCalculatorTest {

    @Test
    public void testCalculNormal() {
        double resultat = PollenCalculator.calculConcentration(10, "olivier", "ete", 20, 50, 10);
        assertEquals(20.16, resultat, 0.01);
    }

    @Test
    public void testPlafond100() {
        double resultat = PollenCalculator.calculConcentration(500, "bouleau", "printemps", 30, 10, 20);
        assertEquals(100.0, resultat);
    }

    @Test
    public void testCouleurFaible() {
        String[] tab = PollenCalculator.calculLibelleCouleur(10);
        assertEquals("Faible", tab[0]);
        assertEquals("#66BB6A", tab[1]);
    }
}