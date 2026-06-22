package MS4.MS4.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import MS4.MS4.dto.AutorDTO;
import MS4.MS4.model.Autor;
import MS4.MS4.model.LibroAutor;
import MS4.MS4.repository.AutorRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public List<AutorDTO> obtenerTodos() {
        return autorRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public List<AutorDTO> buscarPorTituloLibro(String tituloLibro) {
        List<Autor> autores = autorRepository.findByLibroTitulo(tituloLibro);
        if (autores.isEmpty()) {
            throw new RuntimeException("Error: No se encontraron autores para el libro: " + tituloLibro);
        }
        return autores.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public Autor guardar(Autor autor) {
        return autorRepository.save(autor);
    }

    public Autor actualizar(Integer id, Autor autor) {
        Autor aut = autorRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: El autor no existe."));
        if (autor.getNombre() != null) {
            aut.setNombre(autor.getNombre());
        }
        if (autor.getLibroAutor() != null) {
            aut.setLibroAutor(autor.getLibroAutor());
        }
        return autorRepository.save(aut);
    }

    public String eliminar(Integer id) {
        try {
            Autor autor = autorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Error: El autor con ID " + id + " no existe."));
            autorRepository.delete(autor);
            return "El autor " + autor.getNombre() + " ha sido eliminado con exito.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private AutorDTO convertirADTO(Autor Atr) {
        AutorDTO dto = new AutorDTO();
        dto.setId(Atr.getId());
        dto.setNombre(Atr.getNombre());
        List<String> librosEscritos = new ArrayList<>();
        if (Atr.getLibroAutor() != null) {
            for (LibroAutor libroAutor : Atr.getLibroAutor()) {
                librosEscritos.add(libroAutor.getLibro().getTitulo());
            }
        } else {
            librosEscritos.add("El Autor no tiene libros publicados");
        }
        dto.setTituloLibros(librosEscritos);
        return dto;
    }
}