package esiag.back.models.TraficRoutier.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mesure_trafic")
@Data
public class MesureTrafic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double vitesseMoyenne;

    private int nombreVehicules;

    private double tauxOccupation;

    private LocalDateTime dateMesure;

    @ManyToOne
    @JoinColumn(name = "capteur_id")
    private Capteur capteur;
}