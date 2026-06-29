package MS3.MS3.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import MS3.MS3.assemblers.BibliotecaModelAssembler;
import MS3.MS3.dto.BibliotecaDTO;
import MS3.MS3.model.Biblioteca;
import MS3.MS3.service.BibliotecaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Bibliotecas", description = "Operaciones CRUD sobre bibliotecas y consultas por comuna, empleado y préstamo")
@RestController
@RequestMapping("api/v1/bibliotecas")
public class BibliotecaController {

    @Autowired
    private BibliotecaService bibliotecaService;

    @Autowired
    private BibliotecaModelAssembler assembler;

    @Operation(
        summary     = "Listar todas las bibliotecas",
        description = "Retorna la lista completa de bibliotecas con su comuna, clientes con préstamo activo y empleados"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Lista de bibliotecas obtenida exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = BibliotecaDTO.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "No existen bibliotecas registradas",
            content      = @Content
        )
    })
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<BibliotecaDTO>>> obtenerTodas() {
        List<EntityModel<BibliotecaDTO>> bibliotecas = bibliotecaService.obtenerTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (bibliotecas.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                bibliotecas,
                linkTo(methodOn(BibliotecaController.class).obtenerTodas()).withSelfRel()
        ));
    }

    @Operation(
        summary     = "Obtener biblioteca por ID",
        description = "Retorna los datos de una biblioteca específica junto a su comuna, empleados y clientes con préstamo activo"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Biblioteca encontrada exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = BibliotecaDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Biblioteca no encontrada con el ID proporcionado",
            content      = @Content
        )
    })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<BibliotecaDTO>> obtenerPorId(@PathVariable Integer id) {
        BibliotecaDTO dto = bibliotecaService.buscarPorId(id);
        if (dto != null) {
            return ResponseEntity.ok(assembler.toModel(dto));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
        summary     = "Crear nueva biblioteca",
        description = "Registra una nueva biblioteca en la base de datos"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description  = "Biblioteca creada exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = BibliotecaDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description  = "Datos de la biblioteca inválidos o incompletos",
            content      = @Content
        )
    })
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<BibliotecaDTO>> guardarBiblioteca(@Valid @RequestBody Biblioteca biblioteca) {
        try {
            Biblioteca guardada = bibliotecaService.guardar(biblioteca);
            BibliotecaDTO dtoCreado = bibliotecaService.buscarPorId(guardada.getIdBiblioteca());
            return ResponseEntity
                    .created(linkTo(methodOn(BibliotecaController.class).obtenerPorId(dtoCreado.getId())).toUri())
                    .body(assembler.toModel(dtoCreado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary     = "Actualizar biblioteca",
        description = "Modifica los datos completos de una biblioteca existente según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Biblioteca actualizada exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = BibliotecaDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Biblioteca no encontrada con el ID proporcionado",
            content      = @Content
        )
    })
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<BibliotecaDTO>> actualizarBiblioteca(@PathVariable Integer id, @Valid @RequestBody Biblioteca biblioteca) {
        try {
            Biblioteca actualizada = bibliotecaService.actualizar(id, biblioteca);
            BibliotecaDTO dtoEditado = bibliotecaService.buscarPorId(actualizada.getIdBiblioteca());
            return ResponseEntity.ok(assembler.toModel(dtoEditado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Eliminar biblioteca",
        description = "Elimina una biblioteca de la base de datos según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Biblioteca eliminada exitosamente",
            content      = @Content(mediaType = "text/plain",
                           schema = @Schema(type = "string", example = "Biblioteca eliminada con éxito"))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Biblioteca no encontrada con el ID proporcionado",
            content      = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarBiblioteca(@PathVariable Integer id) {
        String mensaje = bibliotecaService.eliminar(id);
        if (mensaje.startsWith("Error")) {
            return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mensaje, HttpStatus.OK);
    }

    @Operation(
        summary     = "Obtener biblioteca por ID de préstamo",
        description = "Retorna la biblioteca asociada a un préstamo específico (consulta externa a MS1)"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Biblioteca del préstamo encontrada exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = BibliotecaDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "No se encontró biblioteca asociada al préstamo indicado",
            content      = @Content
        )
    })
    @GetMapping(value = "/prestamo/{idPrestamo}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<BibliotecaDTO>> obtenerPorPrestamo(@PathVariable Integer idPrestamo) {
        try {
            BibliotecaDTO dto = bibliotecaService.buscarPorIdPrestamo(idPrestamo);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Listar bibliotecas por ID de comuna",
        description = "Retorna todas las bibliotecas que pertenecen a una comuna específica"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Bibliotecas de la comuna obtenidas exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = BibliotecaDTO.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "La comuna no tiene bibliotecas registradas",
            content      = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Comuna no encontrada con el ID proporcionado",
            content      = @Content
        )
    })
    @GetMapping(value = "/comuna/{idComuna}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<BibliotecaDTO>>> obtenerPorComuna(@PathVariable Integer idComuna) {
        try {
            List<EntityModel<BibliotecaDTO>> lista = bibliotecaService.buscarPorIdComuna(idComuna).stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());
            
            if(lista.isEmpty()) return ResponseEntity.noContent().build();

            return ResponseEntity.ok(CollectionModel.of(
                    lista,
                    linkTo(methodOn(BibliotecaController.class).obtenerPorComuna(idComuna)).withSelfRel()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Obtener biblioteca por ID de empleado",
        description = "Retorna la biblioteca a la que está asignado un empleado específico (consulta externa a MS2)"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Biblioteca del empleado encontrada exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = BibliotecaDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "No se encontró biblioteca asociada al empleado indicado",
            content      = @Content
        )
    })
    @GetMapping(value = "/empleado/{idEmpleado}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<BibliotecaDTO>> obtenerPorEmpleado(@PathVariable Integer idEmpleado) {
        try {
            BibliotecaDTO dto = bibliotecaService.buscarPorIdEmpleado(idEmpleado);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}