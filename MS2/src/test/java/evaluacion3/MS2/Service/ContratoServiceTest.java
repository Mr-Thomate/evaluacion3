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

import evaluacion3.MS2.repository.ContratoRepository;
import evaluacion3.MS2.service.ContratoService;
import evaluacion3.MS2.model.Contrato;
import evaluacion3.MS2.dto.ContratoDTO;


@ExtendWith(MockitoExtension.class)
public class ContratoServiceTest {

    @Mock
    private ContratoRepository contratoRepository;

    @InjectMocks
    private ContratoService contratoService;
    
    private Contrato crearContrato() {
        Contrato contrato = new Contrato();
        contrato.setTipoContrato("Contrato de prueba");
        contrato.setSueldo(10000);
        return contrato;
    }

    @Test
    public void testObtenerTodos() {
        Contrato contrato = crearContrato();
        when(contratoRepository.findAll()).thenReturn(List.of(contrato));

        List<ContratoDTO> resultado = contratoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Contrato de prueba", resultado.get(0).getTipoCntrato());
    }

    @Test
    public void testBuscarPorId() {
        Integer id = 1;
        Contrato contrato = crearContrato();
        when(contratoRepository.findById(id)).thenReturn(Optional.of(contrato));

        ContratoDTO resultado = contratoService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals("Contrato de prueba", resultado.getTipoCntrato());
    }

    @Test
    public void testGuardar() {
        Contrato contrato = crearContrato();
        when(contratoRepository.save(contrato)).thenReturn(contrato);

        Contrato resultado = contratoService.guardar(contrato);

        assertNotNull(resultado);
        assertEquals("Contrato de prueba", resultado.getTipoContrato());
    }

    @Test
    public void testActualizar() {
        Contrato Existente = crearContrato();
        Contrato cambios = new Contrato();
        cambios.setTipoContrato("Contrato actualizado");
        cambios.setSueldo(15000);

        when(contratoRepository.findById(1)).thenReturn(Optional.of(Existente));
        when(contratoRepository.save(Existente)).thenReturn(Existente);

        Contrato resultado = contratoService.actualizar(1, cambios);

        assertNotNull(resultado);
        assertEquals("Contrato actualizado", resultado.getTipoContrato());
        assertEquals(15000, resultado.getSueldo());
    }

    @Test
    public void testEliminar() {
        Integer id = 1;
        Contrato contrato = crearContrato();
        when(contratoRepository.findById(id)).thenReturn(Optional.of(contrato));

        String resultado = contratoService.eliminar(id);

        assertEquals("El contrato se elimino con exito.", resultado);
        verify(contratoRepository).delete(contrato);
    }
}