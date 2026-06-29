package evaluacion3.MS2.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import evaluacion3.MS2.assemblers.ContratoModelAssembler;
import evaluacion3.MS2.dto.ContratoDTO;
import evaluacion3.MS2.model.Contrato;
import evaluacion3.MS2.service.ContratoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Contratos", description = "Operaciones CRUD sobre contratos de empleados")
@RestController
@RequestMapping("/api/v1/contratos")
public class ContratoController {
    
    @Autowired
    private ContratoService contratoService;

    @Autowired
    private ContratoModelAssembler assembler;

    @Operation(
        summary     = "Listar todos los contratos",
        description = "Retorna la lista completa de contratos registrados en el sistema"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Lista de contratos obtenida exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = ContratoDTO.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "No existen contratos registrados",
            content      = @Content
        )
    })
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ContratoDTO>>> obtenerTodosContratos() {
        List<EntityModel<ContratoDTO>> contratos = contratoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (contratos.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                contratos,
                linkTo(methodOn(ContratoController.class).obtenerTodosContratos()).withSelfRel()
        ));
    }

    @Operation(
        summary     = "Obtener contrato por ID",
        description = "Retorna los datos de un contrato específico incluyendo el nombre del empleado asociado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Contrato encontrado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = ContratoDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Contrato no encontrado con el ID proporcionado",
            content      = @Content
        )
    })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ContratoDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            ContratoDTO dto = contratoService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Buscar contratos por ID de empleado",
        description = "Retorna todos los contratos asociados a un empleado específico"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Contratos del empleado obtenidos exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = ContratoDTO.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "El empleado no tiene contratos asociados",
            content      = @Content
        )
    })
    @GetMapping(value = "/empleado/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ContratoDTO>>> buscarPorIdEmpleado(@PathVariable Integer id) {
        List<EntityModel<ContratoDTO>> lista = contratoService.buscarPorIdEmpleado(id).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (lista.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                lista,
                linkTo(methodOn(ContratoController.class).buscarPorIdEmpleado(id)).withSelfRel()
        ));
    }

    @Operation(
        summary     = "Crear nuevo contrato",
        description = "Registra un nuevo contrato en la base de datos"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description  = "Contrato creado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = ContratoDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description  = "Datos del contrato inválidos o incompletos",
            content      = @Content
        )
    })
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ContratoDTO>> guardarContrato(@Valid @RequestBody Contrato nuevocontrato) {
        try {
            Contrato guardado = contratoService.guardar(nuevocontrato);
            ContratoDTO dtoCreado = contratoService.buscarPorId(guardado.getId());
            return ResponseEntity
                    .created(linkTo(methodOn(ContratoController.class).obtenerPorId(dtoCreado.getIdContrato())).toUri())
                    .body(assembler.toModel(dtoCreado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary     = "Actualizar contrato",
        description = "Modifica los datos de un contrato existente según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Contrato actualizado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = ContratoDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Contrato no encontrado con el ID proporcionado",
            content      = @Content
        )
    })
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ContratoDTO>> actualizarContrato(@PathVariable Integer id, @Valid @RequestBody Contrato contrato) {
        try {
            Contrato editado = contratoService.actualizar(id, contrato);
            ContratoDTO dtoActualizado = contratoService.buscarPorId(editado.getId());
            return ResponseEntity.ok(assembler.toModel(dtoActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Eliminar contrato",
        description = "Elimina un contrato de la base de datos según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Contrato eliminado exitosamente",
            content      = @Content(mediaType = "text/plain",
                           schema = @Schema(type = "string", example = "Contrato eliminado con éxito"))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Contrato no encontrado con el ID proporcionado",
            content      = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarContrato(@PathVariable Integer id) {
        String resultado = contratoService.eliminar(id);
        if (resultado.contains("exito")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}