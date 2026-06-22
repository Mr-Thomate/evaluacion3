package MS4.MS4.repository;

import org.springframework.stereotype.Repository;
import MS4.MS4.model.Editorial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface EditorialRepository extends JpaRepository<Editorial, Integer>{
    // Busqueda de editorial por libro
    @Query("SELECT e FROM Editorial e JOIN e.libroEditorial le JOIN le.libro l WHERE l.titulo = :tituloLibro")
    List<Editorial> findByLibroTitulo(@Param("tituloLibro") String tituloLibro);
}