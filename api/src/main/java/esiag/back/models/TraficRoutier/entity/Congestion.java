package esiag.back.models.TraficRoutier.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "congestion")
@Data
public class Congestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CongestionNiveau niveau;

    private double vitesseMoyenne;

    private int nombreVehicules;

    private double tauxOccupation;

    private LocalDateTime dateCalcul;

    @ManyToOne
    @JoinColumn(name = "troncon_id")
    private Troncon troncon;
}