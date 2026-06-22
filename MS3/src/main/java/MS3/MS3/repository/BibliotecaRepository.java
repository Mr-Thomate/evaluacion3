package MS3.MS3.repository;

import org.springframework.stereotype.Repository;
import MS3.MS3.model.Biblioteca;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface BibliotecaRepository extends JpaRepository<Biblioteca, Integer>{
    // Busqueda de biblioteca por prestamo asociado
    @Query("SELECT b FROM Biblioteca b JOIN b.prestamo p WHERE p.id = :idPrestamo")
    Optional<Biblioteca> findByIdPrestamo(@Param("idPrestamo") Integer idPrestamo);

    // Busqueda de biblioteca por comuna asociada
    @Query("SELECT b FROM Biblioteca b JOIN b.comuna c WHERE c.id = :idComuna")
    List<Biblioteca> findByIdComuna(@Param("idComuna") Integer idComuna);

    // Busqueda de biblioteca por empleado asociada
    @Query("SELECT b FROM Biblioteca b JOIN b.empleado e WHERE e.id = :idEmpleado")
    Optional<Biblioteca> findByIdEmpleado(@Param("idEmpleado") Integer idEmpleado);
}