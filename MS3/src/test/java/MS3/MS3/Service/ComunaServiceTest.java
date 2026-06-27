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

import MS3.MS3.dto.ComunaDTO;
import MS3.MS3.model.Comuna;
import MS3.MS3.repository.ComunaRepository;
import MS3.MS3.service.ComunaService;



@ExtendWith(MockitoExtension.class)
public class ComunaServiceTest {

    @Mock
    private ComunaRepository comunaRepositoryMock;

    @InjectMocks
    private ComunaService comunaService;
    
    private Comuna crearComuna() {
        Comuna comuna = new Comuna();
        comuna.setIdComuna(1);
        comuna.setNombreComuna("Comuna 1");
        return comuna;
    }

    @Test
    public void testObtenerTodas() {
        Comuna comuna = crearComuna();
        when(comunaRepositoryMock.findAll()).thenReturn(List.of(comuna));
        List<ComunaDTO> resultado = comunaService.obtenerTodas();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Comuna 1", resultado.get(0).getNombreComuna());
    }

    @Test
    public void testBuscarPorId() {
        Integer id = 1;
        Comuna comuna = crearComuna();
        when(comunaRepositoryMock.findById(id)).thenReturn(Optional.of(comuna));
        ComunaDTO resultado = comunaService.buscarPorId(id);
        assertNotNull(resultado);
        assertEquals("Comuna 1", resultado.getNombreComuna());
    }

    @Test
    public void testGuardar() {
        Comuna comuna = new Comuna();
        when(comunaRepositoryMock.save(comuna)).thenReturn(comuna);
        Comuna resultado = comunaService.guardar(comuna);
        assertNotNull(resultado);
    }

    @Test
    public void testActualizar() {
        Integer id = 1;
        Comuna comunaExistente = crearComuna();
        Comuna cambios = new Comuna();
        cambios.setIdComuna(id);
        cambios.setNombreComuna("Comuna Actualizada");
        when(comunaRepositoryMock.findById(id)).thenReturn(java.util.Optional.of(comunaExistente));
        when(comunaRepositoryMock.save(comunaExistente)).thenReturn(comunaExistente);

        Comuna resultado = comunaService.actualizar(id, cambios);
        assertNotNull(resultado);
        assertEquals("Comuna Actualizada", resultado.getNombreComuna());
    }

    @Test
    public void testEliminar() {
        Integer id = 1;
        Comuna comuna = crearComuna();
        when(comunaRepositoryMock.findById(id)).thenReturn(java.util.Optional.of(comuna));
        doNothing().when(comunaRepositoryMock).delete(comuna);

        String resultado = comunaService.eliminar(id);

        assertEquals("La comuna Comuna 1 ha sido eliminada con exito.", resultado);
        verify(comunaRepositoryMock).delete(comuna);
    }

    @Test
    public void buscarBibliotecasEnComuna() {
        String nombreBiblioteca = "Biblioteca Central";
        Comuna comuna = crearComuna();
        when(comunaRepositoryMock.findByNombreBiblioteca(nombreBiblioteca)).thenReturn(List.of(comuna));

        List<ComunaDTO> resultado = comunaService.buscarBibliotecasEnComuna(nombreBiblioteca);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Comuna 1", resultado.get(0).getNombreComuna());
    }
}
