package evaluacion3.MS2.controller;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import evaluacion3.MS2.assemblers.EmpleadoModelAssembler;
import evaluacion3.MS2.dto.EmpleadoDTO;
import evaluacion3.MS2.model.Empleado;
import evaluacion3.MS2.service.EmpleadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Empleados", description = "Operaciones CRUD sobre empleados de la biblioteca")
@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private EmpleadoModelAssembler assembler;

    @Operation(
        summary     = "Listar todos los empleados",
        description = "Retorna la lista completa de empleados con su contrato y biblioteca asociada"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Lista de empleados obtenida exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = EmpleadoDTO.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "No existen empleados registrados",
            content      = @Content
        )
    })
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<EmpleadoDTO>>> obtenerTodosEmpleados() {
        List<EntityModel<EmpleadoDTO>> empleados = empleadoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (empleados.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                empleados,
                linkTo(methodOn(EmpleadoController.class).obtenerTodosEmpleados()).withSelfRel()
        ));
    }

    @Operation(
        summary     = "Obtener empleado por ID",
        description = "Retorna los datos de un empleado específico junto a su contrato y biblioteca"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Empleado encontrado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = EmpleadoDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Empleado no encontrado con el ID proporcionado",
            content      = @Content
        )
    })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EmpleadoDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            EmpleadoDTO dto = empleadoService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Crear nuevo empleado",
        description = "Registra un nuevo empleado en la base de datos"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description  = "Empleado creado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = EmpleadoDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description  = "Datos del empleado inválidos o incompletos",
            content      = @Content
        )
    })
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EmpleadoDTO>> guardarEmpleado(@Valid @RequestBody Empleado nuevoEmpleado) {
        try {
            Empleado guardado = empleadoService.guardar(nuevoEmpleado);
            EmpleadoDTO dtoCreado = empleadoService.buscarPorId(guardado.getId());
            return ResponseEntity
                    .created(linkTo(methodOn(EmpleadoController.class).obtenerPorId(dtoCreado.getIdEmpleado())).toUri())
                    .body(assembler.toModel(dtoCreado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary     = "Actualizar empleado",
        description = "Modifica los datos de un empleado existente según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Empleado actualizado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = EmpleadoDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Empleado no encontrado con el ID proporcionado",
            content      = @Content
        )
    })
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EmpleadoDTO>> actualizarEmpleado(@PathVariable Integer id, @Valid @RequestBody Empleado empleado) {
        try {
            Empleado editado = empleadoService.actualizar(id, empleado);
            EmpleadoDTO dtoEditado = empleadoService.buscarPorId(editado.getId());
            return ResponseEntity.ok(assembler.toModel(dtoEditado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Eliminar empleado",
        description = "Elimina un empleado de la base de datos según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Empleado eliminado exitosamente",
            content      = @Content(mediaType = "text/plain",
                           schema = @Schema(type = "string", example = "Empleado eliminado con éxito"))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Empleado no encontrado con el ID proporcionado",
            content      = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEmpleado(@PathVariable Integer id) {
        try {
            empleadoService.eliminar(id);
            return new ResponseEntity<>("Empleado eliminado con éxito", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error al eliminar", HttpStatus.NOT_FOUND);
        }
    }
}