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
import MS4.MS4.dto.LibroDTO;
import MS4.MS4.model.Libro;
import MS4.MS4.service.LibroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/libros")
public class LibroController {
    @Autowired
    private LibroService libroService;

    // Retornar lista libros
    @GetMapping
    public ResponseEntity<List<LibroDTO>> obtenerTodosLibros() {
        List<LibroDTO> listaLibros = libroService.obtenerTodos();
        if (listaLibros.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listaLibros, HttpStatus.OK);
    }

    // Buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<LibroDTO> obtenerPorId(@PathVariable Integer id) {
        try {
            LibroDTO libro = libroService.buscarPorId(id);
            return new ResponseEntity<>(libro, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar por autor
    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<LibroDTO>> buscarPorAutor(@PathVariable String autor) {
        List<LibroDTO> listaLibros = libroService.buscarPorAutor(autor);
        if (listaLibros.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listaLibros, HttpStatus.OK);
    }
    // Buscar por categoria
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<LibroDTO>> buscarPorCategoria(@PathVariable String categoria) {
        List<LibroDTO> listaLibros = libroService.buscarPorCategoria(categoria);
        if (listaLibros.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listaLibros, HttpStatus.OK);
    }
    // Buscar por editorial
    @GetMapping("/editorial/{editorial}")
    public ResponseEntity<List<LibroDTO>> buscarPorEditorial(@PathVariable String editorial) {
        List<LibroDTO> listaLibros = libroService.buscarPorEditorial(editorial);
        if (listaLibros.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listaLibros, HttpStatus.OK);
    }
    // Buscar por prestamo
    @GetMapping("/prestamo/{id}")
    public ResponseEntity<List<LibroDTO>> buscarPorIdPrestamo(@PathVariable Integer id) {
        List<LibroDTO> listaLibros = libroService.buscarPorPrestamo(id);
        if (listaLibros.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listaLibros, HttpStatus.OK);
    }

    // Guardar nuevo libro
    @PostMapping
    public ResponseEntity<Libro> guardarLibro(@Valid @RequestBody Libro libroNuevo) {
        try {
            Libro guardado = libroService.guardar(libroNuevo);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Editar libro
    @PatchMapping("/{id}")
    public ResponseEntity<Libro> editarLibro(@PathVariable Integer id, @Valid @RequestBody Libro libro) {
        try {
            Libro editado = libroService.guardar(libro);
            return new ResponseEntity<>(editado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar libro
    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable Integer id, @Valid @RequestBody Libro libro) {
        try {
            Libro actualizado = libroService.actualizar(id, libro);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar libro
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarLibro(@PathVariable Integer id) {
        String resultado = libroService.eliminar(id);

        if (resultado.contains("exito")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}