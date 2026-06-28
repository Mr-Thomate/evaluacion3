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

import MS4.MS4.repository.LibroRepository;
import MS4.MS4.service.LibroService;
import MS4.MS4.dto.LibroDTO;
import MS4.MS4.model.Libro;

@ExtendWith(MockitoExtension.class)
public class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroService libroService;

    private Libro crearLibro() {
        Libro libro = new Libro();
        libro.setTitulo("El Principito");
        libro.setFechaPublicacion("1943-04-06");

        return libro;
    }

    @Test
    public void testObtenerTodos() {
        Libro libro = crearLibro();
        when(libroRepository.findAll()).thenReturn(List.of(libro));

        List<LibroDTO> resultado = libroService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("El Principito", resultado.get(0).getTitulo());
    }

    @Test
    public void testBuscarPorId() {
        Integer id = 1;
        Libro libro = crearLibro();
        when(libroRepository.findById(id)).thenReturn(Optional.of(libro));

        LibroDTO resultado = libroService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals("El Principito", resultado.getTitulo());
    }

    @Test
    public void testGuardar() {
        Libro libro = crearLibro();
        when(libroRepository.save(libro)).thenReturn(libro);

        Libro resultado = libroService.guardar(libro);

        assertNotNull(resultado);
        assertEquals("El Principito", resultado.getTitulo());
    }

    @Test
    public void testEliminar() {
        Integer id = 1;
        Libro libro = crearLibro();
        when(libroRepository.findById(id)).thenReturn(Optional.of(libro));
        doNothing().when(libroRepository).delete(libro);

        String resultado = libroService.eliminar(id);

        assertEquals("El libro El Principito ha sido eliminado con exito.", resultado);
        verify(libroRepository).delete(libro);
    }

    @Test
    public void testActualizar() {
        Integer id = 1;
        Libro existente = crearLibro();
        Libro cambios = new Libro();
        cambios.setTitulo("El Principito - Edición Especial");
        cambios.setFechaPublicacion("1943-05-01");

        when(libroRepository.findById(id)).thenReturn(Optional.of(existente));
        when(libroRepository.save(existente)).thenReturn(existente);

        Libro resultado = libroService.actualizar(id, cambios);

        assertNotNull(resultado);
        assertEquals("El Principito - Edición Especial", resultado.getTitulo());
        assertEquals("1943-05-01", resultado.getFechaPublicacion());
    }

    @Test
    public void testBuscarPorAutor() {
        String nombreAutor = "Antoine de Saint-Exupéry";
        Libro libro = crearLibro();
        when(libroRepository.findByNombreAutor(nombreAutor)).thenReturn(List.of(libro));

        List<LibroDTO> resultado = libroService.buscarPorAutor(nombreAutor);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("El Principito", resultado.get(0).getTitulo());
    }

    @Test
    public void testBuscarPorCategoria() {
        String nombreCategoria = "Ficción";
        Libro libro = crearLibro();
        when(libroRepository.findByNombreCategoria(nombreCategoria)).thenReturn(List.of(libro));

        List<LibroDTO> resultado = libroService.buscarPorCategoria(nombreCategoria);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("El Principito", resultado.get(0).getTitulo());
    }

    @Test
    public void testBuscarPorEditorial() {
        String nombreEditorial = "Editorial XYZ";
        Libro libro = crearLibro();
        when(libroRepository.findByNombreEditorial(nombreEditorial)).thenReturn(List.of(libro));

        List<LibroDTO> resultado = libroService.buscarPorEditorial(nombreEditorial);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("El Principito", resultado.get(0).getTitulo());
    }

    @Test
    public void testBuscarPorPrestamo() {
        Integer idPrestamo = 1;
        Libro libro = crearLibro();
        when(libroRepository.findByIdPrestamo(idPrestamo)).thenReturn(List.of(libro));

        List<LibroDTO> resultado = libroService.buscarPorPrestamo(idPrestamo);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("El Principito", resultado.get(0).getTitulo());
    }
}
