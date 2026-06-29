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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Libros", description = "Operaciones CRUD sobre libros y búsquedas por autor, categoría, editorial y préstamo")
@RestController
@RequestMapping("api/v1/libros")
public class LibroController {
    
    @Autowired
    private LibroService libroService;

    @Autowired
    private LibroModelAssembler assembler;

    @Operation(
        summary     = "Listar todos los libros",
        description = "Retorna la lista completa de libros con su autor, editorial, categoría y préstamos asociados"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Lista de libros obtenida exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = LibroDTO.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "No existen libros registrados",
            content      = @Content
        )
    })
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

    @Operation(
        summary     = "Obtener libro por ISBN",
        description = "Retorna los datos de un libro específico junto a su autor, editorial, categoría y clientes con préstamo activo"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Libro encontrado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = LibroDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Libro no encontrado con el ISBN proporcionado",
            content      = @Content
        )
    })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<LibroDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            LibroDTO libro = libroService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(libro));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Buscar libros por nombre de autor",
        description = "Retorna todos los libros escritos por un autor cuyo nombre coincida con el parámetro"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Libros del autor encontrados exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = LibroDTO.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "No se encontraron libros para el autor indicado",
            content      = @Content
        )
    })
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

    @Operation(
        summary     = "Buscar libros por categoría",
        description = "Retorna todos los libros que pertenecen a una categoría específica"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Libros de la categoría encontrados exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = LibroDTO.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "No se encontraron libros para la categoría indicada",
            content      = @Content
        )
    })
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

    @Operation(
        summary     = "Buscar libros por editorial",
        description = "Retorna todos los libros publicados por una editorial cuyo nombre coincida con el parámetro"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Libros de la editorial encontrados exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = LibroDTO.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "No se encontraron libros para la editorial indicada",
            content      = @Content
        )
    })
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

    @Operation(
        summary     = "Buscar libros por ID de préstamo",
        description = "Retorna los libros asociados a un préstamo específico (consulta externa a MS1)"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Libros del préstamo encontrados exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = LibroDTO.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "No se encontraron libros asociados al préstamo indicado",
            content      = @Content
        )
    })
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

    @Operation(
        summary     = "Crear nuevo libro",
        description = "Registra un nuevo libro en la base de datos"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description  = "Libro creado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = LibroDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description  = "Datos del libro inválidos o incompletos",
            content      = @Content
        )
    })
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

    @Operation(
        summary     = "Edición parcial de libro",
        description = "Modifica parcialmente los datos de un libro existente según su ISBN"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Libro editado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = LibroDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Libro no encontrado con el ISBN proporcionado",
            content      = @Content
        )
    })
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

    @Operation(
        summary     = "Actualizar libro",
        description = "Modifica todos los datos de un libro existente según su ISBN"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Libro actualizado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = LibroDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Libro no encontrado con el ISBN proporcionado",
            content      = @Content
        )
    })
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

    @Operation(
        summary     = "Eliminar libro",
        description = "Elimina un libro de la base de datos según su ISBN"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Libro eliminado exitosamente",
            content      = @Content(mediaType = "text/plain",
                           schema = @Schema(type = "string", example = "Libro eliminado con éxito"))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Libro no encontrado con el ISBN proporcionado",
            content      = @Content
        )
    })
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