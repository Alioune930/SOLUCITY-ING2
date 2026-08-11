package esiag.back.models.TraficRoutier.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "troncon")
@Data
public class Troncon {

    @Id                                 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double longueur;

    // @Column(nullable = false)
    // private String nom;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "troncon_id")
    private List<Coordonnee> coordonnees = new ArrayList<>();

    @OneToMany(mappedBy = "troncon", cascade = CascadeType.ALL)
    private List<Congestion> congestions = new ArrayList<>();

    @OneToMany(mappedBy = "troncon", cascade = CascadeType.ALL)
    private List<Evenement> evenements = new ArrayList<>();

    @OneToMany(mappedBy = "troncon", cascade = CascadeType.ALL)
    private List<Voie> voies = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @OneToMany(mappedBy = "troncon", cascade = CascadeType.ALL)
    private List<Capteur> capteurs = new ArrayList<>();
}