package esiag.back.models.AirQuality.entity;

import lombok.Data;
import org.locationtech.jts.geom.MultiPolygon;


import javax.persistence.*;

@Entity
@Data
@Table(name = "zone_ville")
public class ZoneVille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_capteur")
    private String idCapteur;

    @Column(nullable = false)
    private String ville;

    @Column(columnDefinition = "geometry(MULTIPOLYGON, 4326)")
    private MultiPolygon geom;


}
