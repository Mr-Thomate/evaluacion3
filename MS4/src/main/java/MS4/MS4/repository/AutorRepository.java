package MS4.MS4.repository;

import MS4.MS4.model.Autor;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Integer>{
    // Encontrar autor por libro
    @Query("SELECT a FROM Autor a JOIN a.libroAutor la JOIN la.libro l WHERE l.titulo = :tituloLibro")
    List<Autor> findByLibroTitulo(@Param("tituloLibro") String tituloLibro);
}