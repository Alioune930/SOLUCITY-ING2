package esiag.back.models.AirQuality.entity;

import lombok.Data;
import org.locationtech.jts.geom.Point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "capteur_pollution")
@Data
public class CapteurPollution {

    @Id
    @Column(name = "id_capteur", length = 20)
    private String idCapteurPollution;

    @Column(nullable = false)
    private String profil;

    @Column(nullable = false)
    private String ville;

    @Column(nullable = false)
    private Float latitude;

    @Column(nullable = false)
    private Float longitude;

    @Column(columnDefinition = "GEOMETRY(POINT, 4326)")
    private Point geom;
}
