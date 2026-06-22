package MS3.MS3.repository;

import org.springframework.stereotype.Repository;
import MS3.MS3.model.Region;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer>{
    // Busqueda de region por comuna asociado
    @Query("SELECT r FROM Region r JOIN r.comuna c WHERE c.id = :idComuna")
    List<Region> findByidComuna(@Param("idComuna") Integer idComuna);
}