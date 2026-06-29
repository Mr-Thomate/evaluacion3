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

import MS3.MS3.assemblers.ComunaModelAssembler;
import MS3.MS3.dto.ComunaDTO;
import MS3.MS3.model.Comuna;
import MS3.MS3.service.ComunaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Comunas", description = "Operaciones CRUD sobre comunas y consulta de bibliotecas por nombre de comuna")
@RestController
@RequestMapping("api/v1/comunas")
public class ComunaController {
    
    @Autowired
    private ComunaService comunaService;

    @Autowired
    private ComunaModelAssembler assembler;

    @Operation(
        summary     = "Listar todas las comunas",
        description = "Retorna la lista completa de comunas con su región y las bibliotecas que contienen"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Lista de comunas obtenida exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = ComunaDTO.class),
        examples  = @ExampleObject(value = """
            {
              "_embedded": {
                "comunaDTOList": [{
                  "idComuna": 1,
                  "nombreComuna": "Providencia",
                  "region": "Región Metropolitana",
                  "nombreBibliotecas": ["Biblioteca Central", "Biblioteca Sur"]
                }]
              }
            }
        """))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "No existen comunas registradas",
            content      = @Content
        )
    })
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ComunaDTO>>> obtenerTodasComunas() {
        List<EntityModel<ComunaDTO>> comunas = comunaService.obtenerTodas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (comunas.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                comunas,
                linkTo(methodOn(ComunaController.class).obtenerTodasComunas()).withSelfRel()
        ));
    }

    @Operation(
        summary     = "Obtener comuna por ID",
        description = "Retorna los datos de una comuna específica junto a su región y sus bibliotecas"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Comuna encontrada exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = ComunaDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Comuna no encontrada con el ID proporcionado",
            content      = @Content
        )
    })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ComunaDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            ComunaDTO dto = comunaService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Crear nueva comuna",
        description = "Registra una nueva comuna en la base de datos asociándola a una región existente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description  = "Comuna creada exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = ComunaDTO.class),
        examples  = @ExampleObject(value = """
            {
              "idComuna": 5,
              "nombreComuna": "Ñuñoa",
              "region": "Región Metropolitana",
              "nombreBibliotecas": []
            }
        """))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Región asociada no encontrada",
            content      = @Content
        )
    })
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ComunaDTO>> guardarcomuna(@Valid @RequestBody Comuna comunanueva) {
        try {
            Comuna guardada = comunaService.guardar(comunanueva);
            ComunaDTO dtoCreado = comunaService.buscarPorId(guardada.getIdComuna());
            return ResponseEntity
                    .created(linkTo(methodOn(ComunaController.class).obtenerPorId(dtoCreado.getIdComuna())).toUri())
                    .body(assembler.toModel(dtoCreado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Edición parcial de comuna",
        description = "Modifica parcialmente los datos de una comuna existente según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Comuna editada exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = ComunaDTO.class),
        examples  = @ExampleObject(value = """
            {
              "idComuna": 1,
              "nombreComuna": "Providencia Actualizada",
              "region": "Región Metropolitana",
              "nombreBibliotecas": ["Biblioteca Central"]
            }
        """))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Comuna no encontrada con el ID proporcionado",
            content      = @Content
        )
    })
    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ComunaDTO>> editarComuna(@PathVariable Integer id, @Valid @RequestBody Comuna comuna) {
        try {
            Comuna editada = comunaService.guardar(comuna);
            ComunaDTO dtoEditado = comunaService.buscarPorId(editada.getIdComuna());
            return ResponseEntity.ok(assembler.toModel(dtoEditado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Actualizar comuna",
        description = "Modifica todos los datos de una comuna existente según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Comuna actualizada exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = ComunaDTO.class),
        examples  = @ExampleObject(value = """
            {
              "idComuna": 1,
              "nombreComuna": "Providencia Actualizada",
              "region": "Región Metropolitana",
              "nombreBibliotecas": ["Biblioteca Central"]
            }
        """))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Comuna no encontrada con el ID proporcionado",
            content      = @Content
        )
    })
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ComunaDTO>> actualizarComuna(@PathVariable Integer id, @Valid @RequestBody Comuna comuna) {
        try {
            Comuna editada = comunaService.actualizar(id, comuna);
            ComunaDTO dtoActualizado = comunaService.buscarPorId(editada.getIdComuna());
            return ResponseEntity.ok(assembler.toModel(dtoActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Eliminar comuna",
        description = "Elimina una comuna de la base de datos según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode =  "200",
            description  =  "Comuna eliminada exitosamente",
            content      =  @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "Comuna eliminada con éxito"),
                            examples  = @ExampleObject(value = "La comuna Providencia ha sido eliminada con exito.")
        )
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Comuna no encontrada con el ID proporcionado",
            content      = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCategoria(@PathVariable Integer id) {
        String resultado = comunaService.eliminar(id);
        if (resultado.contains("exito")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
        summary     = "Buscar comunas que contienen una biblioteca por nombre",
        description = "Retorna las comunas que tienen una biblioteca cuyo nombre coincide con el parámetro entregado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Comunas con la biblioteca buscada encontradas exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = ComunaDTO.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "No se encontraron comunas con esa biblioteca",
            content      = @Content
        )
    })
    @GetMapping(value = "/biblioteca/{biblioteca}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ComunaDTO>>> buscarBibliotecasEnComuna(@PathVariable String biblioteca) {
        List<EntityModel<ComunaDTO>> listabibliotecas = comunaService.buscarBibliotecasEnComuna(biblioteca).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (listabibliotecas.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                listabibliotecas,
                linkTo(methodOn(ComunaController.class).buscarBibliotecasEnComuna(biblioteca)).withSelfRel()
        ));
    }
}