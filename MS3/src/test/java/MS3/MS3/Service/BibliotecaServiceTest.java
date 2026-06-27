package MS3.MS3.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import MS3.MS3.dto.BibliotecaDTO;
import MS3.MS3.model.Biblioteca;
import MS3.MS3.repository.BibliotecaRepository;
import MS3.MS3.service.BibliotecaService;

@ExtendWith(MockitoExtension.class)
public class BibliotecaServiceTest {

    @Mock
    private BibliotecaRepository bibliotecaRepository;

    @InjectMocks
    private BibliotecaService bibliotecaService;

    private Biblioteca crearBiblioteca() {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.setIdBiblioteca(1);
        biblioteca.setNombreBiblioteca("Biblioteca Central");
        biblioteca.setDireccion("psj. power ranger 123");
        
        return biblioteca;
    }

    @Test
    public void testObtenerTodas() {
        Biblioteca biblioteca = crearBiblioteca();
        when(bibliotecaRepository.findAll()).thenReturn(List.of(biblioteca));

        List<BibliotecaDTO> resultado = bibliotecaService.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Biblioteca Central", resultado.get(0).getNombreBiblioteca());
    }

    @Test
    public void testBuscarPorId() {
        Integer id = 1;
        Biblioteca biblioteca = crearBiblioteca();
        when(bibliotecaRepository.findById(id)).thenReturn(Optional.of(biblioteca));

        BibliotecaDTO resultado = bibliotecaService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals("Biblioteca Central", resultado.getNombreBiblioteca());
    }

    @Test
    public void testGuardar() {
        Biblioteca biblioteca = crearBiblioteca();
        when(bibliotecaRepository.save(biblioteca)).thenReturn(biblioteca);

        Biblioteca resultado = bibliotecaService.guardar(biblioteca);

        assertNotNull(resultado);
        assertEquals("Biblioteca Central", resultado.getNombreBiblioteca());
    }

    @Test
    public void testActualizar() {
        Integer id = 1;
        Biblioteca existente = crearBiblioteca();
        Biblioteca cambios = new Biblioteca();
        cambios.setNombreBiblioteca("Biblioteca Nueva");
        cambios.setDireccion("Calle Falsa 456");

        when(bibliotecaRepository.findById(id)).thenReturn(Optional.of(existente));
        when(bibliotecaRepository.save(existente)).thenReturn(existente);

        Biblioteca resultado = bibliotecaService.actualizar(id, cambios);

        assertNotNull(resultado);
        assertEquals("Biblioteca Nueva", resultado.getNombreBiblioteca());
        assertEquals("Calle Falsa 456", resultado.getDireccion());
    }

    @Test
    public void testEliminar() {
        Integer id = 1;
        Biblioteca biblioteca = crearBiblioteca();
        when(bibliotecaRepository.findById(id)).thenReturn(Optional.of(biblioteca));
        doNothing().when(bibliotecaRepository).delete(biblioteca);

        String resultado = bibliotecaService.eliminar(id);

        assertEquals("La biblioteca Biblioteca se elimino con exito.", resultado);
        verify(bibliotecaRepository).delete(biblioteca);
    }

    @Test
    public void testBuscarPorIdPrestamo() {
        Integer idPrestamo = 1;
        Biblioteca biblioteca = crearBiblioteca();
        when(bibliotecaRepository.findByIdPrestamo(idPrestamo)).thenReturn(Optional.of(biblioteca));

        BibliotecaDTO resultado = bibliotecaService.buscarPorIdPrestamo(idPrestamo);

        assertNotNull(resultado);
        assertEquals("Biblioteca Central", resultado.getNombreBiblioteca());
    }

    @Test
    public void testBuscarPorIdComuna() {
        Integer idComuna = 1;
        Biblioteca biblioteca = crearBiblioteca();
        when(bibliotecaRepository.findByIdComuna(idComuna)).thenReturn(List.of(biblioteca));

        List<BibliotecaDTO> resultado = bibliotecaService.buscarPorIdComuna(idComuna);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    public void buscarPorIdEmpleado() {
        Integer idEmpleado = 1;
        Biblioteca biblioteca = crearBiblioteca();
        when(bibliotecaRepository.findByIdEmpleado(idEmpleado)).thenReturn(Optional.of(biblioteca));

        BibliotecaDTO resultado = bibliotecaService.buscarPorIdEmpleado(idEmpleado);

        assertNotNull(resultado);
        assertEquals("Biblioteca Central", resultado.getNombreBiblioteca());
    }
}