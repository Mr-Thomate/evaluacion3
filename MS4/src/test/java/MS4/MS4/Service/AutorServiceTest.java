package MS4.MS4.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import MS4.MS4.repository.AutorRepository;
import MS4.MS4.service.AutorService;
import MS4.MS4.dto.AutorDTO;
import MS4.MS4.model.Autor;

@ExtendWith(MockitoExtension.class)
public class AutorServiceTest {

    @Mock
    private AutorRepository autorRepository;

    @InjectMocks
    private AutorService autorService;

    private Autor crearAutor() {
        Autor autor = new Autor();
        autor.setNombre("Gabriel García Márquez");
        return autor;
    }

    @Test
    public void testObtenerTodos() {
        Autor autor = crearAutor();
        when(autorRepository.findAll()).thenReturn(List.of(autor));

        List<AutorDTO> resultado = autorService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Gabriel García Márquez", resultado.get(0).getNombre());
    }

    @Test
    public void testBuscarPorId() {
        Integer id = 1;
        Autor autor = crearAutor();
        when(autorRepository.findById(id)).thenReturn(Optional.of(autor));

        AutorDTO resultado = autorService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals("Gabriel García Márquez", resultado.getNombre());
    }

    @Test
    public void guardarAutorTest() {
        Autor autor = crearAutor();
        when(autorRepository.save(autor)).thenReturn(autor);

        Autor resultado = autorService.guardar(autor);

        assertNotNull(resultado);
        assertEquals("Gabriel García Márquez", resultado.getNombre());
    }

    @Test
    public void actualizarAutorTest() {
        Integer id = 1;
        Autor Existente = crearAutor();
        Autor cambios = new Autor();
        cambios.setNombre("Mario Vargas Llosa");

        when(autorRepository.findById(id)).thenReturn(Optional.of(Existente));
        when(autorRepository.save(any(Autor.class))).thenReturn(cambios);

        Autor resultado = autorService.actualizar(id, cambios);

        assertNotNull(resultado);
        assertEquals("Mario Vargas Llosa", resultado.getNombre());
    }

    @Test
    public void eliminarAutorTest() {
        Integer id = 1;
        Autor autor = crearAutor();
        when(autorRepository.findById(id)).thenReturn(Optional.of(autor));
        doNothing().when(autorRepository).delete(autor);

        String resultado = autorService.eliminar(id);

        assertEquals("El autor Gabriel García Márquez ha sido eliminado con exito.", resultado);
        verify(autorRepository, times(1)).delete(autor);
    }

    @Test
    public void buscarPorTituloLibroTest() {
        String tituloLibro = "Cien Años de Soledad";
        Autor autor = crearAutor();
        when(autorRepository.findByLibroTitulo(tituloLibro)).thenReturn(List.of(autor));

        List<AutorDTO> resultado = autorService.buscarPorTituloLibro(tituloLibro);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Gabriel García Márquez", resultado.get(0).getNombre());
    }
}
