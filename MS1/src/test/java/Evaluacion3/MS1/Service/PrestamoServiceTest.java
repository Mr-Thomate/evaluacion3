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

import Evaluacion3.MS1.repository.PrestamoRepository;
import Evaluacion3.MS1.service.PrestamoService;
import Evaluacion3.MS1.dto.PrestamoDTO;
import Evaluacion3.MS1.model.Prestamo;

@ExtendWith(MockitoExtension.class)
public class PrestamoServiceTest {

    @Mock
    private PrestamoRepository prestamoRepository;

    @InjectMocks
    private PrestamoService prestamoService;

    private Prestamo crearPrestamo() {
        Prestamo prestamo = new Prestamo();
        prestamo.setEstado("vigente");
        return prestamo;
    }

    @Test
    public void testObtenerTodos() {
        Prestamo prestamo = crearPrestamo();
        when(prestamoRepository.findAll()).thenReturn(List.of(prestamo));

        List<PrestamoDTO> resultado = prestamoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("vigente", resultado.get(0).getEstado());
    }

    @Test
    public void testBuscarPorId() {
        Integer id = 1;
        Prestamo prestamo = crearPrestamo();
        when(prestamoRepository.findById(id)).thenReturn(Optional.of(prestamo));

        PrestamoDTO resultado = prestamoService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals("vigente", resultado.getEstado());
    }

    @Test
    public void testGuardar() {
        Prestamo prestamo = crearPrestamo();
        when(prestamoRepository.save(prestamo)).thenReturn(prestamo);

        Prestamo resultado = prestamoService.guardar(prestamo);

        assertNotNull(resultado);
        assertEquals("vigente", resultado.getEstado());
    }

    @Test
    public void testActualizar() {
        Integer id = 1;
        Prestamo Existente = crearPrestamo();
        Prestamo cambios = new Prestamo();
        cambios.setEstado("vencido");

        when(prestamoRepository.findById(id)).thenReturn(Optional.of(Existente));
        when(prestamoRepository.save(Existente)).thenReturn(Existente);

        Prestamo resultado = prestamoService.actualizar(id, cambios);

        assertNotNull(resultado);
        assertEquals("vencido", resultado.getEstado());
    }

    @Test
    public void testEliminar() {
        Integer id = 1;
        Prestamo prestamo = crearPrestamo();
        when(prestamoRepository.findById(id)).thenReturn(Optional.of(prestamo));

        String resultado = prestamoService.eliminar(id);

        assertEquals("El préstamo con ID " + id + " ha sido eliminado con exito.", resultado);
        verify(prestamoRepository, times(1)).delete(prestamo);
    }
}