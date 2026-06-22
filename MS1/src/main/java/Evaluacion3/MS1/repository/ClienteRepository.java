package Evaluacion3.MS1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import Evaluacion3.MS1.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    
    @Query("SELECT c FROM Cliente c JOIN c.prestamo p WHERE p.libroId = :libroId")
    List<Cliente> findByLibroId(@Param("libroId") Integer libroId);

    @Query("SELECT c FROM Cliente c JOIN c.prestamo p WHERE p.id = :idPrestamo")
    Cliente findByIdPrestamo(@Param("idPrestamo") Integer idPrestamo);
}