package esiag.back.models.TraficRoutier.entity;

import lombok.Data;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "voie")
@Data
public class Voie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String statut;

    @ManyToOne
    @JoinColumn(name = "troncon_id")
    @JsonIgnore
    private Troncon troncon;
}