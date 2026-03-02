package esiag.back.models.Pollen.entity;

import lombok.Data;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;

@Entity
@Table(name = "capteur_pollen")
@Data
public class CapteurPollen {
    @Id
    @Column(name = "id_capteur", length = 20)
    private String idCapteur;

    @Column
    private String profil;

    @Column(nullable = false)
    private String ville;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double latitude;

    @Column(columnDefinition = "GEOMETRY(POINT,4326)")
    private Point geom;
}
