package esiag.back.models.TraficRoutier.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "action_regulation")
@Data
public class ActionRegulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String typeAction;

    private String description;

    private LocalDateTime dateAction;

    private String statut;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "troncon_id")
    private Troncon troncon;

    @ManyToOne
    @JoinColumn(name = "voie_id")
    private Voie voie;  
}
