package esiag.back.models.TraficRoutier.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "capteur", cascade = CascadeType.ALL)
    private List<MesureTrafic> mesures = new ArrayList<>();
}
