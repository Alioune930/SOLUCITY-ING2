package esiag.back.models.TraficRoutier.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "capteur")
@Data
public class Capteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reference;

    @Column(nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "troncon_id")
    private Troncon troncon;
}
