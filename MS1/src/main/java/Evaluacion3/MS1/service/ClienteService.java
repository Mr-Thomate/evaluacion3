package Evaluacion3.MS1.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Evaluacion3.MS1.dto.ClienteDTO;
import Evaluacion3.MS1.model.Cliente;
import Evaluacion3.MS1.model.Prestamo;
import Evaluacion3.MS1.repository.ClienteRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    
    private final DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public List<ClienteDTO> obtenerTodos() {
        List<ClienteDTO> dtos = new ArrayList<>();
        for (Cliente c : clienteRepository.findAll()) {
            dtos.add(convertirADTO(c));
        }
        return dtos;
    }

    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente actualizar(Integer id, Cliente cliente) {
        Cliente cli = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: El cliente no existe."));
        
        if (cliente.getPnombre() != null) cli.setPnombre(cliente.getPnombre());
        if (cliente.getSnombre() != null) cli.setSnombre(cliente.getSnombre());
        if (cliente.getPapellido() != null) cli.setPapellido(cliente.getPapellido());
        if (cliente.getSapellido() != null) cli.setSapellido(cliente.getSapellido());
        if (cliente.getSexo() != null) cli.setSexo(cliente.getSexo());
        if (cliente.getPrestamo() != null) cli.setPrestamo(cliente.getPrestamo());
        if (cliente.getFechaNacimiento() != null) cli.setFechaNacimiento(cliente.getFechaNacimiento());
        
        return clienteRepository.save(cli);
    }

    public String eliminar(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: El cliente no existe, no se puede eliminar."));
        clienteRepository.delete(cliente);
        return "El cliente con ID: " + id + " ha sido eliminado con éxito.";
    }

    public ClienteDTO buscarPorId(Integer id) {
        return clienteRepository.findById(id).map(this::convertirADTO)
                .orElseThrow(() -> new RuntimeException("Error: No se encontró el cliente con el ID: " + id));
    }

    public List<ClienteDTO> buscarPorIdLibro(Integer libroId) {
        return clienteRepository.findByLibroId(libroId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO buscarPorIdPrestamo(Integer idPrestamo) {
        Cliente cliente = clienteRepository.findByIdPrestamo(idPrestamo);
        if (cliente == null) {
            throw new RuntimeException("Error: No existe un cliente asociado al préstamo ID: " + idPrestamo);
        }
        return convertirADTO(cliente);
    }

    private ClienteDTO convertirADTO(Cliente c) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(c.getId());
        dto.setPnombre(c.getPnombre());
        dto.setSnombre(c.getSnombre());
        dto.setPapellido(c.getPapellido());
        dto.setSapellido(c.getSapellido());
        dto.setSexo(c.getSexo());
        if (c.getFechaNacimiento() != null) {
            dto.setFechaNacimiento(c.getFechaNacimiento().format(formateador));
        }
        if (c.getPrestamo() != null) {
            dto.setIdPrestamosAsociados(c.getPrestamo().stream()
                    .map(Prestamo::getId)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}