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

import MS3.MS3.assemblers.RegionModelAssembler;
import MS3.MS3.dto.RegionDTO;
import MS3.MS3.model.Region;
import MS3.MS3.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Regiones", description = "Operaciones CRUD sobre regiones del país")
@RestController
@RequestMapping("api/v1/regiones")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @Autowired
    private RegionModelAssembler assembler;

    @Operation(
        summary     = "Listar todas las regiones",
        description = "Retorna la lista completa de regiones con las comunas que contienen"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Lista de regiones obtenida exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = RegionDTO.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "No existen regiones registradas",
            content      = @Content
        )
    })
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<RegionDTO>>> obtenerTodas() {
        List<EntityModel<RegionDTO>> regiones = regionService.obtenerTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (regiones.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                regiones,
                linkTo(methodOn(RegionController.class).obtenerTodas()).withSelfRel()
        ));
    }

    @Operation(
        summary     = "Obtener región por ID",
        description = "Retorna los datos de una región específica junto a sus comunas"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Región encontrada exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = RegionDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Región no encontrada con el ID proporcionado",
            content      = @Content
        )
    })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RegionDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            RegionDTO dto = regionService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Crear nueva región",
        description = "Registra una nueva región en la base de datos"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description  = "Región creada exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = RegionDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description  = "Datos de la región inválidos o incompletos",
            content      = @Content
        )
    })
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RegionDTO>> guardarRegion(@Valid @RequestBody Region region) {
        try {
            Region guardada = regionService.guardar(region);
            RegionDTO dtoCreado = regionService.buscarPorId(guardada.getIdRegion());
            return ResponseEntity
                    .created(linkTo(methodOn(RegionController.class).obtenerPorId(dtoCreado.getIdRegion())).toUri())
                    .body(assembler.toModel(dtoCreado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary     = "Edición parcial de región",
        description = "Modifica parcialmente los datos de una región existente según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Región editada exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = RegionDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Región no encontrada con el ID proporcionado",
            content      = @Content
        )
    })
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RegionDTO>> editarRegion(@PathVariable Integer id, @Valid @RequestBody Region region) {
        try {
            Region editada = regionService.guardar(region);
            RegionDTO dtoEditado = regionService.buscarPorId(editada.getIdRegion());
            return ResponseEntity.ok(assembler.toModel(dtoEditado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Actualizar región",
        description = "Modifica todos los datos de una región existente según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Región actualizada exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = RegionDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Región no encontrada con el ID proporcionado",
            content      = @Content
        )
    })
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RegionDTO>> actualizarLibro(@PathVariable Integer id, @Valid @RequestBody Region region) {
        try {
            Region actualizada = regionService.actualizar(id, region);
            RegionDTO dtoActualizado = regionService.buscarPorId(actualizada.getIdRegion());
            return ResponseEntity.ok(assembler.toModel(dtoActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Eliminar región",
        description = "Elimina una región de la base de datos según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Región eliminada exitosamente",
            content      = @Content(mediaType = "text/plain",
                           schema = @Schema(type = "string", example = "La región ha sido eliminada exitosamente."))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Región no encontrada con el ID proporcionado",
            content      = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRegion(@PathVariable Integer id) {
        try {
            regionService.eliminar(id);
            return new ResponseEntity<>("La región ha sido eliminada exitosamente.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}