package Evaluacion3.MS1.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import Evaluacion3.MS1.dto.BibliotecaExternoDTO;
import Evaluacion3.MS1.dto.LibroExternoDTO;
import Evaluacion3.MS1.dto.PrestamoDTO;
import Evaluacion3.MS1.model.Prestamo;
import Evaluacion3.MS1.repository.PrestamoRepository;
import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;
    
    private final DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public List<PrestamoDTO> obtenerTodos() {
        return prestamoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public PrestamoDTO buscarPorId(Integer id) {
        return prestamoRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Error: No se encontró el préstamo con el ID: " + id));
    }

    public Prestamo guardar(Prestamo prestamo) {
        return prestamoRepository.save(prestamo);
    }

    public Prestamo actualizar(Integer id, Prestamo prestamo) {
        Prestamo p = prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: El préstamo no existe."));
        
        if (prestamo.getFechaInicio() != null) p.setFechaInicio(prestamo.getFechaInicio());
        if (prestamo.getFechaDevolucion() != null) p.setFechaDevolucion(prestamo.getFechaDevolucion());
        if (prestamo.getEstado() != null) p.setEstado(prestamo.getEstado());
        if (prestamo.getCliente() != null) p.setCliente(prestamo.getCliente());
        if (prestamo.getLibroId() != null) p.setLibroId(prestamo.getLibroId());
        if (prestamo.getBibliotecaId() != null) p.setBibliotecaId(prestamo.getBibliotecaId());
        
        return prestamoRepository.save(p);
    }

    public String eliminar(Integer id) {
        try {
            Prestamo prestamo = prestamoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Error: No se puede eliminar el préstamo, pues no existe."));
            prestamoRepository.delete(prestamo);
            return "El préstamo con ID " + id + " ha sido eliminado con exito.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private PrestamoDTO convertirADTO(Prestamo prestamo) {
        PrestamoDTO dto = new PrestamoDTO();
        dto.setIdPrestamo(prestamo.getId());
        dto.setEstado(prestamo.getEstado());
        
        if (prestamo.getFechaInicio() != null) dto.setFechaIncio(prestamo.getFechaInicio().format(formateador));
        if (prestamo.getFechaDevolucion() != null) dto.setFechaFin(prestamo.getFechaDevolucion().format(formateador));
        
        if (prestamo.getCliente() != null) {
            dto.setClienteIdCliente(prestamo.getCliente().getId());
            dto.setNombreClienteCompleto(prestamo.getCliente().getPnombre() + " " + prestamo.getCliente().getPapellido());
        }
        
        try {
            LibroExternoDTO libroGuardado = webClientBuilder.build()
                .get()
                .uri("http://localhost:8083/api/v1/libros/" + prestamo.getLibroId()) 
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToMono(LibroExternoDTO.class)
                .block();

            dto.setLibro(libroGuardado);
        } catch (Exception e) {
            dto.setLibro(null);
        }
        try {
            BibliotecaExternoDTO bibliotecaGuardado = webClientBuilder.build()
                .get()
                .uri("http://localhost:8084/api/v1/bibliotecas/" + prestamo.getBibliotecaId()) 
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToMono(BibliotecaExternoDTO.class)
                .block();

            dto.setBiblioteca(bibliotecaGuardado);
        } catch (Exception e) {
            dto.setBiblioteca(null); 
        }
        
        return dto;
    }
}
