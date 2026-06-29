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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import MS4.MS4.assemblers.EditorialModelAssembler;
import MS4.MS4.dto.EditorialDTO;
import MS4.MS4.model.Editorial;
import MS4.MS4.service.EditorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Editoriales", description = "Operaciones CRUD sobre editoriales y búsqueda por título de libro")
@RestController
@RequestMapping("api/v1/editoriales")
public class EditorialController {

    @Autowired
    private EditorialService editorialService;

    @Autowired
    private EditorialModelAssembler assembler;

    @Operation(
        summary     = "Listar todas las editoriales",
        description = "Retorna la lista completa de editoriales con los títulos de sus libros"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Lista de editoriales obtenida exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = EditorialDTO.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "No existen editoriales registradas",
            content      = @Content
        )
    })
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<EditorialDTO>>> obtenerTodas() {
        List<EntityModel<EditorialDTO>> editoriales = editorialService.obtenerTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (editoriales.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                editoriales,
                linkTo(methodOn(EditorialController.class).obtenerTodas()).withSelfRel()
        ));
    }

    @Operation(
        summary     = "Obtener editorial por ID",
        description = "Retorna los datos de una editorial específica junto a los títulos de sus libros"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Editorial encontrada exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = EditorialDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Editorial no encontrada con el ID proporcionado",
            content      = @Content
        )
    })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EditorialDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            EditorialDTO dto = editorialService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Buscar editoriales por título de libro",
        description = "Retorna las editoriales que publicaron un libro cuyo título coincida con el parámetro de búsqueda"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Editoriales del libro encontradas exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = EditorialDTO.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "No se encontraron editoriales para el título indicado",
            content      = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Libro no encontrado con el título proporcionado",
            content      = @Content
        )
    })
    @GetMapping(value = "/busqueda", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<EditorialDTO>>> buscarPorLibro(@RequestParam String titulo) {
        try {
            List<EntityModel<EditorialDTO>> lista = editorialService.buscarPorTituloLibro(titulo).stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());
                    
            if(lista.isEmpty()) return ResponseEntity.noContent().build();

            return ResponseEntity.ok(CollectionModel.of(
                    lista,
                    linkTo(methodOn(EditorialController.class).buscarPorLibro(titulo)).withSelfRel()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Crear nueva editorial",
        description = "Registra una nueva editorial en la base de datos"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description  = "Editorial creada exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = EditorialDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description  = "Datos de la editorial inválidos o incompletos",
            content      = @Content
        )
    })
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EditorialDTO>> guardarEditorial(@Valid @RequestBody Editorial editorial) {
        try {
            Editorial guardada = editorialService.guardar(editorial);
            EditorialDTO dtoCreado = editorialService.buscarPorId(guardada.getId());
            return ResponseEntity
                    .created(linkTo(methodOn(EditorialController.class).obtenerPorId(dtoCreado.getIdEditorial())).toUri())
                    .body(assembler.toModel(dtoCreado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary     = "Edición parcial de editorial",
        description = "Modifica parcialmente los datos de una editorial existente según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Editorial editada exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = EditorialDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Editorial no encontrada con el ID proporcionado",
            content      = @Content
        )
    })
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EditorialDTO>> editarEditorial(@PathVariable Integer id, @Valid @RequestBody Editorial editorial) {
        try {
            Editorial editada = editorialService.guardar(editorial);
            EditorialDTO dtoEditado = editorialService.buscarPorId(editada.getId());
            return ResponseEntity.ok(assembler.toModel(dtoEditado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Actualizar editorial",
        description = "Modifica todos los datos de una editorial existente según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Editorial actualizada exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = EditorialDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Editorial no encontrada con el ID proporcionado",
            content      = @Content
        )
    })
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EditorialDTO>> actualizarEditorial(@PathVariable Integer id, @Valid @RequestBody Editorial editorial) {
        try {
            Editorial editada = editorialService.actualizar(id, editorial);
            EditorialDTO dtoActualizado = editorialService.buscarPorId(editada.getId());
            return ResponseEntity.ok(assembler.toModel(dtoActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Eliminar editorial",
        description = "Elimina una editorial de la base de datos según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Editorial eliminada exitosamente",
            content      = @Content(mediaType = "text/plain",
                           schema = @Schema(type = "string", example = "Editorial eliminada correctamente."))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Editorial no encontrada con el ID proporcionado",
            content      = @Content
        )
    })
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