package esiag.back.models.TraficRoutier.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "coordonnee")
@Data
public class Coordonnee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private int ordre;
}