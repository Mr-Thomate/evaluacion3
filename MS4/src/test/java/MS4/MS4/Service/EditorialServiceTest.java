package MS4.MS4.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import MS4.MS4.repository.EditorialRepository;
import MS4.MS4.service.EditorialService;
import MS4.MS4.dto.EditorialDTO;
import MS4.MS4.model.Editorial;

@ExtendWith(MockitoExtension.class)
public class EditorialServiceTest {

    @Mock
    private EditorialRepository editorialRepository;

    @InjectMocks
    private EditorialService editorialService;

    private Editorial crearEditorial() {
        Editorial editorial = new Editorial();
        editorial.setNombre("Editorial bacan");
        return editorial;
    }

    @Test
    public void testObtenerTodas() {
        Editorial editorial = crearEditorial();
        when(editorialRepository.findAll()).thenReturn(List.of(editorial));

        List<EditorialDTO> resultado = editorialService.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Editorial bacan", resultado.get(0).getNombre());
    }

    @Test
    public void testBuscarPorId() {
        Integer id = 1;
        Editorial editorial = crearEditorial();
        when(editorialRepository.findById(id)).thenReturn(Optional.of(editorial));

        EditorialDTO resultado = editorialService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals("Editorial bacan", resultado.getNombre());
    }

    @Test
    public void testGuardar() {
        Editorial editorial = crearEditorial();
        when(editorialRepository.save(editorial)).thenReturn(editorial);

        Editorial resultado = editorialService.guardar(editorial);

        assertNotNull(resultado);
        assertEquals("Editorial bacan", resultado.getNombre());
    }

    @Test
    public void testActualizar() {
        Integer id = 1;
        Editorial Existente = crearEditorial();
        Editorial cambios = new Editorial();
        cambios.setNombre("Editorial actualizada");

        when(editorialRepository.findById(id)).thenReturn(Optional.of(Existente));
        when(editorialRepository.save(any(Editorial.class))).thenReturn(cambios);

        Editorial resultado = editorialService.actualizar(id, cambios);

        assertNotNull(resultado);
        assertEquals("Editorial actualizada", resultado.getNombre());
    }

    @Test
    public void testEliminar() {
        Integer id = 1;
        Editorial editorial = crearEditorial();
        when(editorialRepository.findById(id)).thenReturn(Optional.of(editorial));
        doNothing().when(editorialRepository).delete(editorial);

        String resultado = editorialService.eliminar(id);

        assertEquals("La editorial Editorial bacan ha sido eliminada con exito.", resultado);
        verify(editorialRepository).delete(editorial);
    }

    @Test
    public void testBuscarPorTituloLibro() {
        String tituloLibro = "Libro de prueba";
        Editorial editorial = crearEditorial();
        when(editorialRepository.findByLibroTitulo(tituloLibro)).thenReturn(List.of(editorial));

        List<EditorialDTO> resultado = editorialService.buscarPorTituloLibro(tituloLibro);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Editorial bacan", resultado.get(0).getNombre());
    }

}
