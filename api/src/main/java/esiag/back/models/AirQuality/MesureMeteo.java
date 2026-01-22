package esiag.back.models.AirQuality;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "mesure_meteo")
@Data
public class MesureMeteo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMesure;

    @Column(name = "id_capteur", nullable = false)
    private String idCapteur;

    private Float vitesseVent;
    private Float temperature;
    private Float humidite;
}
