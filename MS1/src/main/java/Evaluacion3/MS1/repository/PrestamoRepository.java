package Evaluacion3.MS1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Evaluacion3.MS1.model.Prestamo;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {
    
    @Query("SELECT p FROM Prestamo p WHERE p.bibliotecaId = :idBiblioteca")
    List<Prestamo> findByIdBiblioteca(@Param("idBiblioteca") Integer idBiblioteca);
    
    @Query("SELECT p FROM Prestamo p WHERE p.cliente.id = :idCliente")
    List<Prestamo> findByIdCliente(@Param("idCliente") Integer idCliente);

    @Query("SELECT p FROM Prestamo p WHERE p.libroId = :libroId")
    List<Prestamo> findByLibroId(@Param("libroId") Integer libroId);
}