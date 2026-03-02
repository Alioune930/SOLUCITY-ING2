package esiag.back.models.Pollen.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "mesure_pollen")
@Data
public class MesurePollen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMesure;

    @Column(name = "id_capteur", nullable = false)
    private String idCapteur;  

    private String saison;
    private String typePollen;
    private Double indiceFloraison;
}
