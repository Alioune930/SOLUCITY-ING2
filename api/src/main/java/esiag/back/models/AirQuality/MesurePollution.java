package esiag.back.models.AirQuality;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "mesure_pollution")
@Data
public class MesurePollution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMesure;

    @Column(name = "id_capteur", nullable = false)
    private String idCapteurPollution;

    private Float no2;

    private Float pm10;

    private Float pm25;

}
