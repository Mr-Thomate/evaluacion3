package evaluacion3.MS2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import evaluacion3.MS2.model.Contrato;
import evaluacion3.MS2.model.Empleado;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Integer>{
    // Busqueda de contrato mediante empleado asociada
    @Query("SELECT c FROM Contrato c JOIN c.empleado e WHERE e.id = :idEmpleado")
    List<Empleado> findByIdEmpleado(@Param("idEmpleado") Integer idEmpleado);
}
