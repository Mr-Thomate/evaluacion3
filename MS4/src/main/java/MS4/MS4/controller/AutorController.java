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

import MS4.MS4.assemblers.AutorModelAssembler;
import MS4.MS4.dto.AutorDTO;
import MS4.MS4.model.Autor;
import MS4.MS4.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Autores", description = "Operaciones CRUD sobre autores y búsqueda por título de libro")
@RestController
@RequestMapping("/api/v1/autores")
public class AutorController {
    
    @Autowired
    private AutorService autorService;

    @Autowired
    private AutorModelAssembler assembler;

    @Operation(
        summary     = "Listar todos los autores",
        description = "Retorna la lista completa de autores con los títulos de sus libros"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Lista de autores obtenida exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = AutorDTO.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "No existen autores registrados",
            content      = @Content
        )
    })
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<AutorDTO>>> obtenerTodosAutores() {
        List<EntityModel<AutorDTO>> autores = autorService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (autores.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                autores,
                linkTo(methodOn(AutorController.class).obtenerTodosAutores()).withSelfRel()
        ));
    }

    @Operation(
        summary     = "Obtener autor por ID",
        description = "Retorna los datos de un autor específico junto a los títulos de sus libros"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Autor encontrado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = AutorDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Autor no encontrado con el ID proporcionado",
            content      = @Content
        )
    })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AutorDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            AutorDTO dto = autorService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Buscar autores por título de libro",
        description = "Retorna los autores asociados a un libro cuyo título coincida con el parámetro"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Autores del libro encontrados exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = AutorDTO.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "No se encontraron autores para el título indicado",
            content      = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Libro no encontrado con el título proporcionado",
            content      = @Content
        )
    })
    @GetMapping(value = "/libro/{titulo}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<AutorDTO>>> obtenerAutorPorTituloLibro(@PathVariable String titulo) {
        try {
            List<EntityModel<AutorDTO>> autores = autorService.buscarPorTituloLibro(titulo).stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());
                    
            if(autores.isEmpty()) return ResponseEntity.noContent().build();
            
            return ResponseEntity.ok(CollectionModel.of(
                    autores,
                    linkTo(methodOn(AutorController.class).obtenerAutorPorTituloLibro(titulo)).withSelfRel()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Crear nuevo autor",
        description = "Registra un nuevo autor en la base de datos"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description  = "Autor creado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = AutorDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description  = "Datos del autor inválidos o incompletos",
            content      = @Content
        )
    })
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AutorDTO>> guardarAutor(@Valid @RequestBody Autor autor) {
        try {
            Autor guardado = autorService.guardar(autor);
            AutorDTO dtoCreado = autorService.buscarPorId(guardado.getId());
            return ResponseEntity
                    .created(linkTo(methodOn(AutorController.class).obtenerPorId(dtoCreado.getId())).toUri())
                    .body(assembler.toModel(dtoCreado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary     = "Edición parcial de autor",
        description = "Modifica parcialmente los datos de un autor existente según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Autor editado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = AutorDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Autor no encontrado con el ID proporcionado",
            content      = @Content
        )
    })
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AutorDTO>> editarAutor(@PathVariable Integer id, @Valid @RequestBody Autor autor) {
        try {
            Autor editado = autorService.guardar(autor);
            AutorDTO dtoEditado = autorService.buscarPorId(editado.getId());
            return ResponseEntity.ok(assembler.toModel(dtoEditado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Actualizar autor",
        description = "Modifica todos los datos de un autor existente según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Autor actualizado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = AutorDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Autor no encontrado con el ID proporcionado",
            content      = @Content
        )
    })
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AutorDTO>> actualizarAutor(@PathVariable Integer id, @Valid @RequestBody Autor autor) {
        try {
            Autor actualizado = autorService.actualizar(id, autor);
            AutorDTO dtoActualizado = autorService.buscarPorId(actualizado.getId());
            return ResponseEntity.ok(assembler.toModel(dtoActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Eliminar autor",
        description = "Elimina un autor de la base de datos según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Autor eliminado exitosamente",
            content      = @Content(mediaType = "text/plain",
                           schema = @Schema(type = "string", example = "Autor eliminado con éxito"))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Autor no encontrado con el ID proporcionado",
            content      = @Content
        )
    })
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