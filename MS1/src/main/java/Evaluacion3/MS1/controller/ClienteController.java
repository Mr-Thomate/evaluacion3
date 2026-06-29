package Evaluacion3.MS1.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Evaluacion3.MS1.assemblers.ClienteModelAssembler;
import Evaluacion3.MS1.dto.ClienteDTO;
import Evaluacion3.MS1.model.Cliente;
import Evaluacion3.MS1.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Clientes", description = "Operaciones CRUD sobre clientes y búsquedas por libro y préstamo")
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteModelAssembler assembler;

    @Operation(
        summary     = "Listar todos los clientes",
        description = "Retorna la lista completa de clientes con sus préstamos asociados"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Lista de clientes obtenida exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = ClienteDTO.class),
            examples  = @ExampleObject(value = """
                {
                  "_embedded": {
                    "clienteDTOList": [{
                      "id": 1,
                      "pnombre": "Juan",
                      "snombre": "Carlos",
                      "papellido": "Pérez",
                      "sapellido": "Gómez",
                      "fechaNacimiento": "15-03-1990",
                      "sexo": "Masculino",
                      "idPrestamosAsociados": [1, 2]
                    }]
                  }
                }
            """))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "No existen clientes registrados",
            content      = @Content
        )
    })
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ClienteDTO>>> obtenerTodos() {
        List<EntityModel<ClienteDTO>> clientes = clienteService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (clientes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(CollectionModel.of(
                clientes,
                linkTo(methodOn(ClienteController.class).obtenerTodos()).withSelfRel()
        ));
    }

    @Operation(
        summary     = "Obtener cliente por ID",
        description = "Retorna los datos de un cliente específico junto a sus préstamos asociados"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Cliente encontrado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = ClienteDTO.class),
            examples  = @ExampleObject(value = """
                {
                  "id": 1,
                  "pnombre": "Juan",
                  "snombre": "Carlos",
                  "papellido": "Pérez",
                  "sapellido": "Gómez",
                  "fechaNacimiento": "15-03-1990",
                  "sexo": "Masculino",
                  "idPrestamosAsociados": [1, 2]
                }
            """)
        )
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Cliente no encontrado con el ID proporcionado",
            content      = @Content
        )
    })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClienteDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            ClienteDTO dto = clienteService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Buscar clientes por ID de libro",
        description = "Retorna los clientes que tienen un préstamo activo del libro indicado (consulta externa a MS4)"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Clientes con préstamo del libro encontrados exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = ClienteDTO.class))
        ),
        @ApiResponse(
            responseCode = "204",
            description  = "No se encontraron clientes con préstamo del libro indicado",
            content      = @Content
        )
    })
    @GetMapping(value = "/libro/{libroId}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<ClienteDTO>>> buscarPorIdLibro(@PathVariable Integer libroId) {
        List<EntityModel<ClienteDTO>> clientes = clienteService.buscarPorIdLibro(libroId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (clientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(CollectionModel.of(
                clientes,
                linkTo(methodOn(ClienteController.class).buscarPorIdLibro(libroId)).withSelfRel()
        ));
    }

    @Operation(
        summary     = "Buscar cliente por ID de préstamo",
        description = "Retorna el cliente asociado a un préstamo específico"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Cliente del préstamo encontrado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = ClienteDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "No se encontró cliente asociado al préstamo indicado",
            content      = @Content
        )
    })
    @GetMapping(value = "/prestamo/{idPrestamo}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClienteDTO>> buscarPorIdPrestamo(@PathVariable Integer idPrestamo) {
        try {
            ClienteDTO dto = clienteService.buscarPorIdPrestamo(idPrestamo);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Crear nuevo cliente",
        description = "Registra un nuevo cliente en la base de datos con validación de fecha de nacimiento"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description  = "Cliente creado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = ClienteDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description  = "Datos del cliente inválidos o incompletos",
            content      = @Content
        )
    })
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClienteDTO>> guardarCliente(@Valid @RequestBody Cliente clienteNuevo) {
        try {
            Cliente guardado = clienteService.guardar(clienteNuevo); 
            ClienteDTO dtoCreado = clienteService.buscarPorId(guardado.getId());
            
            return ResponseEntity
                    .created(linkTo(methodOn(ClienteController.class).obtenerPorId(dtoCreado.getId())).toUri())
                    .body(assembler.toModel(dtoCreado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary     = "Actualizar cliente",
        description = "Modifica todos los datos de un cliente existente según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Cliente actualizado exitosamente",
            content      = @Content(mediaType = "application/hal+json",
                           schema = @Schema(implementation = ClienteDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Cliente no encontrado con el ID proporcionado",
            content      = @Content
        )
    })
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClienteDTO>> actualizarCliente(@PathVariable Integer id, @Valid @RequestBody Cliente cliente) {
        try {
            Cliente editado = clienteService.actualizar(id, cliente);
            ClienteDTO dtoActualizado = clienteService.buscarPorId(editado.getId());
            
            return ResponseEntity.ok(assembler.toModel(dtoActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary     = "Eliminar cliente",
        description = "Elimina un cliente de la base de datos según su ID"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode =  "200",
            description  =  "Cliente eliminado exitosamente",
            content      =  @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string", example = "Cliente eliminado con éxito"),
                            examples  = @ExampleObject(value = "El cliente con ID: 1 ha sido eliminado con éxito.")
        )
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Cliente no encontrado con el ID proporcionado",
            content      = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCliente(@PathVariable Integer id) {
        try {
            String msg = clienteService.eliminar(id);
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}