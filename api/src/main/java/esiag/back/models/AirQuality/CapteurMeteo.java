package esiag.back.models.AirQuality;

import lombok.Data;
import org.locationtech.jts.geom.Point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "capteur_meteo")
@Data
public class CapteurMeteo {

    @Id
    @Column(name = "id_capteur", length = 20)
    private String idCapteurMeteo;

    @Column(nullable = false)
    private String lieu;

    @Column(nullable = false)
    private String ville;

    @Column(nullable = false)
    private Float latitude;

    @Column(nullable = false)
    private Float longitude;

    @Column(columnDefinition = "GEOMETRY(POINT, 4326)")
    private Point geom;

}
