package MS4.MS4.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import MS4.MS4.model.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Integer>{
    // Busqueda de libro por autor
    @Query("SELECT l FROM Libro l JOIN l.libroAutor la JOIN la.autor a WHERE a.nombre = :nombreAutor")
    List<Libro> findByNombreAutor(@Param("nombreAutor") String nombreAutor);

    // Busqueda de libro por Categoria
    @Query("SELECT l FROM Libro l JOIN l.libroCategoria lc JOIN lc.categoria c WHERE c.nombre = :nombreCategoria")
    List<Libro> findByNombreCategoria(@Param("nombreCategoria") String nombreCategoria);

    // Busqueda de libro por Editorial
    @Query("SELECT l FROM Libro l JOIN l.libroEditorial le JOIN le.editorial e WHERE e.nombre = :nombreEditorial")
    List<Libro> findByNombreEditorial(@Param("nombreEditorial") String nombreEditorial);

    // Busqueda de libro por Prestamo asociado
    @Query("SELECT l FROM Libro l JOIN l.prestamo p WHERE p.id = :idPrestamo")
    List<Libro> findByIdPrestamo(@Param("idPrestamo") Integer idPrestamo);

    // Busqueda de libro por Biblioteca asociado
    @Query("SELECT l FROM Libro l JOIN l.prestamo p JOIN p.biblioteca b WHERE b.id = :idBiblioteca")
    List<Libro> findByIdBiblioteca(@Param("idBiblioteca") Integer idBiblioteca);
}