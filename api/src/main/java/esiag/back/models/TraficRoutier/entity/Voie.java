package esiag.back.models.TraficRoutier.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "voie")
@Data
public class Voie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "troncon_id")
    private Troncon troncon;
}
