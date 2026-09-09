package esiag.back.models.TraficRoutier.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "evenement")
@Data
public class Evenement {

    @Id
    private String id;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "troncon_id")
    private Troncon troncon;
}