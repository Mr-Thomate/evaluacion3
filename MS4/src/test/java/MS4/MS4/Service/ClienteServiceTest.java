package Evaluacion3.MS1.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import Evaluacion3.MS1.repository.ClienteRepository;
import Evaluacion3.MS1.service.ClienteService;
import Evaluacion3.MS1.dto.ClienteDTO;
import Evaluacion3.MS1.model.Cliente;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente crearCliente() {
        Cliente cliente = new Cliente();
        cliente.setPnombre("Juan");
        cliente.setSnombre("Carlos");
        cliente.setPapellido("Pérez");
        cliente.setSapellido("Gómez");
        cliente.setSexo("Masculino");
        return cliente;
    }

    @Test
    public void testObtenerTodos() {
        Cliente cliente = crearCliente();
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        List<ClienteDTO> resultado = clienteService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getPnombre());
    }

    @Test
    public void testBuscarPorId() {
        Integer id = 1;
        Cliente cliente = crearCliente();
        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        ClienteDTO resultado = clienteService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getPnombre());
    }

    @Test
    public void testGuardar() {
        Cliente cliente = crearCliente();
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.guardar(cliente);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getPnombre());
    }

    @Test
    public void testActualizar() {
        Integer id = 1;
        Cliente Existente = crearCliente();
        Cliente cambios = new Cliente();
        cambios.setPnombre("Pedro");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(Existente));
        when(clienteRepository.save(Existente)).thenReturn(Existente);

        Cliente resultado = clienteService.actualizar(id, cambios);

        assertNotNull(resultado);
        assertEquals("Pedro", resultado.getPnombre());
    }

    @Test
    public void testEliminar() {
        Integer id = 1;
        Cliente cliente = crearCliente();
        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
        doNothing().when(clienteRepository).delete(cliente);

        String resultado = clienteService.eliminar(id);

        assertNotNull(resultado);
        assertEquals("El cliente con ID: 1 ha sido eliminado con éxito.", resultado);
        verify(clienteRepository).delete(cliente);
    }

    @Test
    public void buscarPorIdLibro() {
        Integer libroId = 1;
        Cliente cliente = crearCliente();
        when(clienteRepository.findByLibroId(libroId)).thenReturn(List.of(cliente));

        List<ClienteDTO> resultado = clienteService.buscarPorIdLibro(libroId);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Juan", resultado.get(0).getPnombre());
    }

    
    @Test
    public void buscarPorIdPrestamo() {
        Integer idPrestamo = 1;
        Cliente cliente = crearCliente();
        when(clienteRepository.findByIdPrestamo(idPrestamo)).thenReturn(cliente);

        ClienteDTO resultado = clienteService.buscarPorIdPrestamo(idPrestamo);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getPnombre());
    }
}
