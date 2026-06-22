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
import org.springframework.web.bind.annotation.RestController;
import MS4.MS4.dto.AutorDTO;
import MS4.MS4.model.Autor;
import MS4.MS4.service.AutorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/autores")
public class AutorController {
    @Autowired
    private AutorService autorService;

    // Metodos
    @GetMapping
    public ResponseEntity<List<AutorDTO>> obtenerTodosAutores() {
        List<AutorDTO> resultado = autorService.obtenerTodos();
        if (resultado.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }

    @GetMapping("/libro/{titulo}")
    public ResponseEntity<List<AutorDTO>> obtenerAutorPorTituloLibro(@PathVariable String titulo) {
        try {
            List<AutorDTO> autores = autorService.buscarPorTituloLibro(titulo);
            return new ResponseEntity<>(autores, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Autor> guardarAutor(@Valid @RequestBody Autor autor) {
        try {
            Autor guardado = autorService.guardar(autor);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Autor> editarAutor(@PathVariable Integer id, @Valid @RequestBody Autor autor) {
        try {
            Autor editado = autorService.guardar(autor);
            return new ResponseEntity<>(editado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Autor> actualizarAutor(@PathVariable Integer id, @Valid @RequestBody Autor autor) {
        try {
            Autor actualizado = autorService.actualizar(id, autor);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarAutor(@PathVariable Integer id) {
        String resultado = autorService.eliminar(id);

        if (resultado.contains("exito")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}