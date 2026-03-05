package esiag.back.repositories.Pollen;

import esiag.back.models.Pollen.dto.DonneePollenLight;
import esiag.back.models.Pollen.entity.MesurePollen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MesurePollenRepository extends JpaRepository<MesurePollen, Long> {
    @Query(value = "SELECT mp.id_capteur AS idCapteur, mp.type_pollen AS typePollen, mp.indice_floraison AS indiceFloraison, mp.saison AS saison, mp.timestamp AS timestamp FROM mesure_pollen mp " +
                   "WHERE mp.id_capteur = :idCap " +
                   "ORDER BY mp.timestamp DESC LIMIT 1", nativeQuery = true)
    DonneePollenLight findLatestByCapteur(@Param("idCap") String idCapteur);
}