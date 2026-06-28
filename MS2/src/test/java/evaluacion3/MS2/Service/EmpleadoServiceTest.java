package evaluacion3.MS2.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import evaluacion3.MS2.repository.EmpleadoRepository;
import evaluacion3.MS2.service.EmpleadoService;
import evaluacion3.MS2.model.Empleado;
import evaluacion3.MS2.dto.EmpleadoDTO;



@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoService empleadoService;

    private Empleado crearEmpleado() {
        Empleado empleado = new Empleado();
        empleado.setPnombre("juan");
        empleado.setSnombre("rodrigo");
        empleado.setPapellido("perez");
        empleado.setSapellido("gomez");
        return empleado;
    }

    @Test
    public void testObtenerTodos() {
        Empleado empleado = crearEmpleado();
        when(empleadoRepository.findAll()).thenReturn(List.of(empleado));

        List<EmpleadoDTO> resultado = empleadoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("juan", resultado.get(0).getPnombre());
        assertEquals("rodrigo", resultado.get(0).getSnombre());
        assertEquals("perez", resultado.get(0).getPapellido());
        assertEquals("gomez", resultado.get(0).getSapellido());
    }

    @Test
    public void testBuscarPorId() {
        Integer id = 1;
        Empleado empleado = crearEmpleado();
        when(empleadoRepository.findById(id)).thenReturn(Optional.of(empleado));

        EmpleadoDTO resultado = empleadoService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals("juan", resultado.getPnombre());
        assertEquals("rodrigo", resultado.getSnombre());
        assertEquals("perez", resultado.getPapellido());
        assertEquals("gomez", resultado.getSapellido());
    }

    @Test
    public void testGuardar() {
        Empleado empleado = crearEmpleado();
        when(empleadoRepository.save(empleado)).thenReturn(empleado);

        Empleado resultado = empleadoService.guardar(empleado);

        assertNotNull(resultado);
        assertEquals("juan", resultado.getPnombre());
        assertEquals("rodrigo", resultado.getSnombre());
        assertEquals("perez", resultado.getPapellido());
        assertEquals("gomez", resultado.getSapellido());
    }

    @Test
    public void testActualizar() {
        Integer id = 1;
        Empleado existente = crearEmpleado();
        Empleado cambios = new Empleado();
        cambios.setPnombre("carlos");
        cambios.setSnombre("andres");
        cambios.setPapellido("lopez");
        cambios.setSapellido("martinez");

        when(empleadoRepository.findById(id)).thenReturn(Optional.of(existente));
        when(empleadoRepository.save(existente)).thenReturn(existente);

        Empleado resultado = empleadoService.actualizar(id, cambios);

        assertNotNull(resultado);
        assertEquals("carlos", resultado.getPnombre());
        assertEquals("andres", resultado.getSnombre());
        assertEquals("lopez", resultado.getPapellido());
        assertEquals("martinez", resultado.getSapellido());
    }

    @Test
    public void testEliminar() {
        Integer id = 1;
        Empleado empleado = crearEmpleado();
        when(empleadoRepository.findById(id)).thenReturn(Optional.of(empleado));

        String resultado = empleadoService.eliminar(id);

        assertEquals("El empleado juan perez ha sido eliminado con exito.", resultado);
        verify(empleadoRepository).delete(empleado);
    }
}
