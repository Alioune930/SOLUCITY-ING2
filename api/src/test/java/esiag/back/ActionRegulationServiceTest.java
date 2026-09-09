package esiag.back;

import esiag.back.models.TraficRoutier.dto.ActionRegulationSimulationDTO;
import esiag.back.models.TraficRoutier.entity.ActionRegulation;
import esiag.back.models.TraficRoutier.entity.Congestion;
import esiag.back.models.TraficRoutier.entity.CongestionNiveau;
import esiag.back.models.TraficRoutier.entity.Troncon;
import esiag.back.models.TraficRoutier.entity.Voie;
import esiag.back.repositories.TraficRoutier.ActionRegulationRepository;
import esiag.back.repositories.TraficRoutier.CongestionRepository;
import esiag.back.repositories.TraficRoutier.VoieRepository;
import esiag.back.services.TraficRoutier.ActionRegulationService;
import esiag.back.services.TraficRoutier.CongestionService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActionRegulationServiceTest {

    @Mock
    private ActionRegulationRepository actionRegulationRepository;

    @Mock
    private CongestionRepository congestionRepository;

    @Mock
    private VoieRepository voieRepository;

    @Mock
    private CongestionService congestionService;

    @InjectMocks
    private ActionRegulationService actionRegulationService;

    @Test
    void simulerFermetureVoie() {
        Long tronconId = 308L;
        Long voieId = 1L;

        Troncon troncon = new Troncon();
        troncon.setId(tronconId);

        Voie voie = new Voie();
        voie.setId(voieId);
        voie.setNom("Voie 1");
        voie.setStatut("OUVERTE");
        voie.setTroncon(troncon);

        Voie autreVoie = new Voie();
        autreVoie.setId(2L);
        autreVoie.setNom("Voie 2");
        autreVoie.setStatut("OUVERTE");
        autreVoie.setTroncon(troncon);

        Congestion congestion = new Congestion();
        congestion.setNiveau(CongestionNiveau.values()[0]);
        congestion.setTauxOccupation(38.0);
        congestion.setVitesseMoyenne(35.0);

        when(congestionRepository.findByTronconId(tronconId))
                .thenReturn(Optional.of(congestion));

        when(voieRepository.findByTronconId(tronconId))
                .thenReturn(List.of(voie, autreVoie));

        when(voieRepository.findById(voieId))
                .thenReturn(Optional.of(voie));

        ActionRegulationSimulationDTO resultat =
                actionRegulationService.simuler(
                        tronconId,
                        voieId,
                        "FERMER_VOIE");

        assertEquals(308L, resultat.getTronconId());
        assertEquals(1L, resultat.getVoieId());
        assertEquals("FERMER_VOIE", resultat.getTypeAction());
        assertEquals(2, resultat.getVoiesOuvertesAvant());
        assertEquals(1, resultat.getVoiesOuvertesApres());
        assertEquals(38.0, resultat.getOccupationAvant());
        assertEquals(45.6, resultat.getOccupationApres());
    }

    @Test
    void simulerOuvertureVoie() {
        Long tronconId = 308L;
        Long voieId = 1L;

        Troncon troncon = new Troncon();
        troncon.setId(tronconId);

        Voie voie = new Voie();
        voie.setId(voieId);
        voie.setNom("Voie 1");
        voie.setStatut("FERMEE");
        voie.setTroncon(troncon);

        Voie autreVoie = new Voie();
        autreVoie.setId(2L);
        autreVoie.setNom("Voie 2");
        autreVoie.setStatut("OUVERTE");
        autreVoie.setTroncon(troncon);

        Congestion congestion = new Congestion();
        congestion.setNiveau(CongestionNiveau.values()[0]);
        congestion.setTauxOccupation(38.0);
        congestion.setVitesseMoyenne(35.0);

        when(congestionRepository.findByTronconId(tronconId))
                .thenReturn(Optional.of(congestion));

        when(voieRepository.findByTronconId(tronconId))
                .thenReturn(List.of(voie, autreVoie));

        when(voieRepository.findById(voieId))
                .thenReturn(Optional.of(voie));

        ActionRegulationSimulationDTO resultat =
                actionRegulationService.simuler(
                        tronconId,
                        voieId,
                        "OUVRIR_VOIE");

        assertEquals("OUVRIR_VOIE", resultat.getTypeAction());
        assertEquals(1, resultat.getVoiesOuvertesAvant());
        assertEquals(2, resultat.getVoiesOuvertesApres());
        assertEquals(38.0, resultat.getOccupationAvant());
        assertEquals(30.4, resultat.getOccupationApres());
    }

    @Test
    void refuserFermetureDerniereVoie() {
        Long tronconId = 308L;
        Long voieId = 1L;

        Troncon troncon = new Troncon();
        troncon.setId(tronconId);

        Voie voie = new Voie();
        voie.setId(voieId);
        voie.setNom("Voie 1");
        voie.setStatut("OUVERTE");
        voie.setTroncon(troncon);

        Congestion congestion = new Congestion();
        congestion.setNiveau(CongestionNiveau.values()[0]);
        congestion.setTauxOccupation(38.0);
        congestion.setVitesseMoyenne(35.0);

        when(congestionRepository.findByTronconId(tronconId))
                .thenReturn(Optional.of(congestion));

        when(voieRepository.findByTronconId(tronconId))
                .thenReturn(List.of(voie));

        when(voieRepository.findById(voieId))
                .thenReturn(Optional.of(voie));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> actionRegulationService.simuler(
                                tronconId,
                                voieId,
                                "FERMER_VOIE"));

        assertEquals(
                "Impossible de fermer la dernière voie ouverte",
                exception.getMessage());
    }

    @Test
    void refuserVoieNappartenantPasAuTroncon() {
        Long tronconId = 308L;
        Long autreTronconId = 309L;
        Long voieId = 1L;

        Troncon troncon = new Troncon();
        troncon.setId(autreTronconId);

        Voie voie = new Voie();
        voie.setId(voieId);
        voie.setNom("Voie 1");
        voie.setStatut("OUVERTE");
        voie.setTroncon(troncon);

        Congestion congestion = new Congestion();
        congestion.setNiveau(CongestionNiveau.values()[0]);
        congestion.setTauxOccupation(38.0);
        congestion.setVitesseMoyenne(35.0);

        when(congestionRepository.findByTronconId(tronconId))
                .thenReturn(Optional.of(congestion));

        when(voieRepository.findByTronconId(tronconId))
                .thenReturn(List.of(voie));

        when(voieRepository.findById(voieId))
                .thenReturn(Optional.of(voie));

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> actionRegulationService.simuler(
                                tronconId,
                                voieId,
                                "FERMER_VOIE"));

        assertEquals(
                "La voie ne correspond pas au tronçon",
                exception.getMessage());
    }

    @Test
    void appliquerFermetureVoie() {
        Long tronconId = 308L;
        Long voieId = 1L;

        Troncon troncon = new Troncon();
        troncon.setId(tronconId);

        Voie voie = new Voie();
        voie.setId(voieId);
        voie.setNom("Voie 1");
        voie.setStatut("OUVERTE");
        voie.setTroncon(troncon);

        Voie autreVoie = new Voie();
        autreVoie.setId(2L);
        autreVoie.setNom("Voie 2");
        autreVoie.setStatut("OUVERTE");
        autreVoie.setTroncon(troncon);

        when(voieRepository.findById(voieId))
                .thenReturn(Optional.of(voie));

        when(voieRepository.findByTronconId(tronconId))
                .thenReturn(List.of(voie, autreVoie));

        ActionRegulation action = new ActionRegulation();

        when(actionRegulationRepository.save(any(ActionRegulation.class)))
                .thenReturn(action);

        ActionRegulation resultat =
                actionRegulationService.appliquer(
                        tronconId,
                        voieId,
                        "FERMER_VOIE");

        assertSame(action, resultat);
        assertEquals("FERMEE", voie.getStatut());

        verify(voieRepository).save(voie);
        verify(congestionService)
                .actualiserApresRegulation(
                        tronconId,
                        "FERMER_VOIE");

        verify(actionRegulationRepository)
                .save(any(ActionRegulation.class));
    }
}