package MS4.MS4.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import MS4.MS4.dto.EditorialDTO;
import MS4.MS4.model.Editorial;
import MS4.MS4.repository.EditorialRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EditorialService {

    @Autowired
    private EditorialRepository editorialRepository;

    public List<EditorialDTO> obtenerTodas() {
        return editorialRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public EditorialDTO buscarPorId(Integer id) {
        return editorialRepository.findById(id).map(this::convertirADTO).orElseThrow(() -> new RuntimeException("Error: No se encontró la editorial con el ID: " + id));
    }

    public List<EditorialDTO> buscarPorTituloLibro(String tituloLibro) {
        List<Editorial> editoriales = editorialRepository.findByLibroTitulo(tituloLibro);

        if (editoriales.isEmpty()) {
            throw new RuntimeException("Error: No se encontraron editoriales para el libro: " + tituloLibro);
        }

        return editoriales.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public Editorial guardar(Editorial editorial) {
        return editorialRepository.save(editorial);
    }

    public Editorial actualizar(Integer id, Editorial editorial) {
        Editorial edi = editorialRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: La editorial no existe."));
        if (editorial.getNombre() != null) {
            edi.setNombre(editorial.getNombre());
        }
        if (editorial.getLibroEditorial() != null) {
            edi.setLibroEditorial(editorial.getLibroEditorial());
        }
        return editorialRepository.save(edi);
    }

    public String eliminar(Integer id) {
        try {
            Editorial edi = editorialRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Error: No se puede eliminar la editorial, pues no existe."));
            editorialRepository.delete(edi);
            return "La editorial " + edi.getNombre() + " ha sido eliminada con exito.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    private EditorialDTO convertirADTO(Editorial editorial) {
        EditorialDTO dto = new EditorialDTO();
        dto.setIdEditorial(editorial.getId());
        dto.setNombre(editorial.getNombre());
        if (editorial.getLibroEditorial() != null) {
            dto.setTituloLibros(editorial.getLibroEditorial().stream().map(le -> le.getLibro().getTitulo()).collect(Collectors.toList()));
        }
        return dto;
    }
}