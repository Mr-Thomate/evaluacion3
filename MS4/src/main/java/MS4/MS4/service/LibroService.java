package MS4.MS4.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import MS4.MS4.dto.LibroDTO;
import MS4.MS4.model.Libro;
import MS4.MS4.repository.LibroRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    public List<LibroDTO> obtenerTodos() {
        return libroRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public LibroDTO buscarPorId(Integer id) {
        return libroRepository.findById(id).map(this::convertirADTO).orElseThrow(() -> new RuntimeException("Error, No se encontró el libro con el ID: " + id));
    }

    public List<LibroDTO> buscarPorAutor(String nombreAutor) {
        return libroRepository.findByNombreAutor(nombreAutor).stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public List<LibroDTO> buscarPorCategoria(String nombreCategoria) {
        return libroRepository.findByNombreCategoria(nombreCategoria).stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public List<LibroDTO> buscarPorEditorial(String nombreEditorial) {
        return libroRepository.findByNombreEditorial(nombreEditorial).stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public List<LibroDTO> buscarPorPrestamo(Integer idPrestamo) {
        return libroRepository.findByIdPrestamo(idPrestamo).stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public Libro guardar(Libro libro) {
        return libroRepository.save(libro);
    }

    public String eliminar(Integer id) {
        try {
            Libro libro = libroRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Error: No se puede eliminar el libro, pues no existe."));
            libroRepository.delete(libro);
            return "El libro " + libro.getTitulo() + " ha sido eliminado con exito.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public Libro actualizar(Integer id, Libro libro) {
        Libro lib = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: El libro no existe."));
        if (libro.getTitulo() != null) lib.setTitulo(libro.getTitulo());
        if (libro.getFechaPublicacion() != null) lib.setFechaPublicacion(libro.getFechaPublicacion());
        if (libro.getLibroCategoria() != null) lib.setLibroCategoria(libro.getLibroCategoria());
        if (libro.getLibroEditorial() != null) lib.setLibroEditorial(libro.getLibroEditorial());
        if (libro.getLibroAutor() != null) lib.setLibroAutor(libro.getLibroAutor());
        
        return libroRepository.save(lib);
    }

    private LibroDTO convertirADTO(Libro libro) {
        LibroDTO dto = new LibroDTO();
        dto.setIsbn(libro.getIsbn());
        dto.setTitulo(libro.getTitulo());
        dto.setFechaPublicacion(libro.getFechaPublicacion());
        if (libro.getLibroCategoria() != null && !libro.getLibroCategoria().isEmpty()) {
            dto.setCategoria(libro.getLibroCategoria().get(0).getCategoria().getNombre());
        }
        if (libro.getLibroEditorial() != null && !libro.getLibroEditorial().isEmpty()) {
            dto.setNombreEditorial(libro.getLibroEditorial().get(0).getEditorial().getNombre());
        }
        if (libro.getLibroAutor() != null && !libro.getLibroAutor().isEmpty()) {
            dto.setNombreAutor(libro.getLibroAutor().get(0).getAutor().getNombre());
        }
        return dto;
    }
}