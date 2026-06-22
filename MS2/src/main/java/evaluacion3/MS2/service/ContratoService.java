package evaluacion3.MS2.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import evaluacion3.MS2.dto.ContratoDTO;
import evaluacion3.MS2.model.Contrato;
import evaluacion3.MS2.model.Empleado;
import evaluacion3.MS2.repository.ContratoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ContratoService {

    @Autowired
    private ContratoRepository contratoRepository;

    public List<ContratoDTO> obtenerTodos() {
        return contratoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ContratoDTO buscarPorId(Integer id) {
        return contratoRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Error: No se encontró el contrato con el ID: " + id));
    }

    public List<ContratoDTO> buscarPorIdEmpleado(Integer idEmpleado) {
        List<Empleado> empleados = contratoRepository.findByIdEmpleado(idEmpleado);

        if (empleados.isEmpty()) {
            throw new RuntimeException("Error: No se encontró contrato para el empleado ID: " + idEmpleado);
        }

        return empleados.stream().map(emp -> convertirADTO(emp.getContrato())).collect(Collectors.toList());
    }

    public Contrato guardar(Contrato contrato) {
        return contratoRepository.save(contrato);
    }

    public Contrato actualizar(Integer id, Contrato contrato) {
        Contrato con = contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: El contrato no existe."));
        if (contrato.getTipoContrato() != null) {
            con.setTipoContrato(contrato.getTipoContrato());
        }
        if (contrato.getFechaInicio() != null) {
            con.setFechaInicio(contrato.getFechaInicio());
        }
        if (contrato.getFechaFin() != null) {
            con.setFechaFin(contrato.getFechaFin());
        }
        if (contrato.getSueldo() != null) {
            con.setSueldo(contrato.getSueldo());
        }
        if (contrato.getEmpleado() != null) {
            con.setEmpleado(contrato.getEmpleado());
        }
        return contratoRepository.save(con);
    }

    public String eliminar(Integer id) {
        try {
            Contrato contrato = contratoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Error: No se encuentra la comuna"));
            contratoRepository.delete(contrato);
            return "El contrtato " + contrato.getId() + " ha sido eliminado con exito.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private ContratoDTO convertirADTO(Contrato contrato) {
        if (contrato == null) {
            return null;
        }

        ContratoDTO dto = new ContratoDTO();
        dto.setIdContrato(contrato.getId());
        dto.setTipoCntrato(contrato.getTipoContrato());
        dto.setFechaInicio(contrato.getFechaInicio().toString());
        dto.setFechaFin(contrato.getFechaFin().toString());
        dto.setSueldo(contrato.getSueldo());
        if (contrato.getEmpleado() != null) {
            String nombreCompleto = contrato.getEmpleado().getPnombre() + " "
                    + contrato.getEmpleado().getPapellido();
            dto.setNombreEmpleado(nombreCompleto);
        }
        return dto;
    }
}
