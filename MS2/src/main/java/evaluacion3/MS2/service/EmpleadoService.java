package evaluacion3.MS2.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import evaluacion3.MS2.dto.BibliotecaExternoDTO;
import evaluacion3.MS2.dto.EmpleadoDTO;
import evaluacion3.MS2.model.Empleado;
import evaluacion3.MS2.repository.EmpleadoRepository;
import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<EmpleadoDTO> obtenerTodos() {
        List<EmpleadoDTO> listaDTO = new ArrayList<>();
        for (Empleado emp : empleadoRepository.findAll()) {
            listaDTO.add(convertirADTO(emp));
        }
        return listaDTO;
    }

    public Empleado guardar(Empleado nuevoEmpleado) {
        return empleadoRepository.save(nuevoEmpleado);
    }

    public String eliminar(Integer id) {
        try {
            Empleado empleado = empleadoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Error: No se encuentra el empleado"));
            empleadoRepository.delete(empleado);
            return "El empleado " + empleado.getId() + " ha sido eliminado con exito.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public Empleado actualizar(Integer id, Empleado empleado) {
        Empleado emp = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: El empleado no existe."));
        if (empleado.getPnombre() != null) {
            emp.setPnombre(empleado.getPnombre());
        }
        if (empleado.getSnombre() != null) {
            emp.setSnombre(empleado.getSnombre());
        }
        if (empleado.getPapellido() != null) {
            emp.setPapellido(empleado.getPapellido());
        }
        if (empleado.getSapellido() != null) {
            emp.setSapellido(empleado.getSapellido());
        }
        if (empleado.getBibliotecaId() != null) {
            emp.setBibliotecaId(empleado.getBibliotecaId());
        }
        if (empleado.getContrato() != null) {
            emp.setContrato(empleado.getContrato());
        }
        return empleadoRepository.save(emp);
    }

    private EmpleadoDTO convertirADTO(Empleado emp) {
        EmpleadoDTO dto = new EmpleadoDTO();

        dto.setIdEmpleado(emp.getId());
        dto.setPnombre(emp.getPnombre());
        dto.setSnombre(emp.getSnombre());
        dto.setPapellido(emp.getPapellido());
        dto.setSapellido(emp.getSapellido());

        try{
            BibliotecaExternoDTO bibliotecaGuardado = webClientBuilder.build()
                .get()
                .uri("http://api/v1/MS3/id/" + emp.getBibliotecaId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty()) // importante
                .bodyToMono(BibliotecaExternoDTO.class)
                .block();

            dto.setBiblioteca(bibliotecaGuardado);
        }catch (Exception e){
            dto.setBiblioteca(null);
        }
        if (emp.getContrato() != null) {
            dto.setEstadoContrato("Con contrato");
        } else {
            dto.setEstadoContrato("Sin contrato");
        }

        return dto;
    }
}
