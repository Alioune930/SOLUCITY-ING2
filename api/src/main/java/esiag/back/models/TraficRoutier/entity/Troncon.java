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
    private String id;

    @Column(nullable = false)
    private double length;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "troncon_id")
    private List<Coordonnee> coordonnees = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Congestion congestion;

    @OneToMany(mappedBy = "troncon", cascade = CascadeType.ALL)
    private List<Evenement> evenements = new ArrayList<>();
}