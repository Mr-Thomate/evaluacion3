package MS4.MS4.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import MS4.MS4.dto.EditorialDTO;
import MS4.MS4.model.Editorial;
import MS4.MS4.service.EditorialService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/editoriales")
public class EditorialController {

    @Autowired
    private EditorialService editorialService;

    @GetMapping
    public ResponseEntity<List<EditorialDTO>> obtenerTodas() {
        List<EditorialDTO> lista = editorialService.obtenerTodas();
        if (lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditorialDTO> obtenerPorId(@PathVariable Integer id) {
        try {
            EditorialDTO dto = editorialService.buscarPorId(id);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/busqueda")
    public ResponseEntity<List<EditorialDTO>> buscarPorLibro(@RequestParam String titulo) {
        try {
            List<EditorialDTO> lista = editorialService.buscarPorTituloLibro(titulo);
            return new ResponseEntity<>(lista, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Editorial> guardarEditorial(@Valid @RequestBody Editorial editorial) {
        try {
            Editorial guardada = editorialService.guardar(editorial);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Editorial> editarEditorial(@PathVariable Integer id, @Valid @RequestBody Editorial editorial) {
        try {
            Editorial editada = editorialService.guardar(editorial);
            return new ResponseEntity<>(editada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Editorial> actualizarEditorial(@PathVariable Integer id, @Valid @RequestBody Editorial editorial) {
        try {
            Editorial editada = editorialService.actualizar(id, editorial);
            return new ResponseEntity<>(editada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEditorial(@PathVariable Integer id) {
        try {
            editorialService.eliminar(id);
            return new ResponseEntity<>("Editorial eliminada correctamente.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}