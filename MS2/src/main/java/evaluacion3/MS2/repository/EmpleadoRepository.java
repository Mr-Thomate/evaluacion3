package evaluacion3.MS2.repository;

import org.springframework.stereotype.Repository;
import evaluacion3.MS2.model.Empleado;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer>{
    // Busqueda de empleado por contrato asociado
    @Query("SELECT e FROM Empleado e JOIN e.contrato c WHERE c.id = :idContrato")
    List<Empleado> findByIdContrato(@Param("idContrato") Integer idContrato);

    // Busqueda de empleado por biblioteca asociado
    @Query("SELECT e FROM Empleado e JOIN e.biblioteca b WHERE b.id = :idBiblioteca")
    List<Empleado> findByIdBiblioteca(@Param("idBiblioteca") Integer idBiblioteca);
}
