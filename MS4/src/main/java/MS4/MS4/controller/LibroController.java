package MS4.MS4.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
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

import MS4.MS4.assemblers.LibroModelAssembler;
import MS4.MS4.dto.LibroDTO;
import MS4.MS4.model.Libro;
import MS4.MS4.service.LibroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/libros")
public class LibroController {
    
    @Autowired
    private LibroService libroService;

    @Autowired
    private LibroModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<LibroDTO>>> obtenerTodosLibros() {
        List<EntityModel<LibroDTO>> libros = libroService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (libros.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                libros,
                linkTo(methodOn(LibroController.class).obtenerTodosLibros()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<LibroDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            LibroDTO libro = libroService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(libro));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/autor/{autor}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<LibroDTO>>> buscarPorAutor(@PathVariable String autor) {
        List<EntityModel<LibroDTO>> libros = libroService.buscarPorAutor(autor).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (libros.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                libros,
                linkTo(methodOn(LibroController.class).buscarPorAutor(autor)).withSelfRel()
        ));
    }

    @GetMapping(value = "/categoria/{categoria}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<LibroDTO>>> buscarPorCategoria(@PathVariable String categoria) {
        List<EntityModel<LibroDTO>> libros = libroService.buscarPorCategoria(categoria).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (libros.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                libros,
                linkTo(methodOn(LibroController.class).buscarPorCategoria(categoria)).withSelfRel()
        ));
    }

    @GetMapping(value = "/editorial/{editorial}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<LibroDTO>>> buscarPorEditorial(@PathVariable String editorial) {
        List<EntityModel<LibroDTO>> libros = libroService.buscarPorEditorial(editorial).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (libros.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                libros,
                linkTo(methodOn(LibroController.class).buscarPorEditorial(editorial)).withSelfRel()
        ));
    }

    @GetMapping(value = "/prestamo/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<LibroDTO>>> buscarPorIdPrestamo(@PathVariable Integer id) {
        List<EntityModel<LibroDTO>> libros = libroService.buscarPorPrestamo(id).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (libros.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                libros,
                linkTo(methodOn(LibroController.class).buscarPorIdPrestamo(id)).withSelfRel()
        ));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<LibroDTO>> guardarLibro(@Valid @RequestBody Libro libroNuevo) {
        try {
            Libro guardado = libroService.guardar(libroNuevo);
            LibroDTO dtoCreado = libroService.buscarPorId(guardado.getIsbn());
            return ResponseEntity
                    .created(linkTo(methodOn(LibroController.class).obtenerPorId(dtoCreado.getIsbn())).toUri())
                    .body(assembler.toModel(dtoCreado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<LibroDTO>> editarLibro(@PathVariable Integer id, @Valid @RequestBody Libro libro) {
        try {
            Libro editado = libroService.guardar(libro);
            LibroDTO dtoEditado = libroService.buscarPorId(editado.getIsbn());
            return ResponseEntity.ok(assembler.toModel(dtoEditado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<LibroDTO>> actualizarLibro(@PathVariable Integer id, @Valid @RequestBody Libro libro) {
        try {
            Libro actualizado = libroService.actualizar(id, libro);
            LibroDTO dtoActualizado = libroService.buscarPorId(actualizado.getIsbn());
            return ResponseEntity.ok(assembler.toModel(dtoActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

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