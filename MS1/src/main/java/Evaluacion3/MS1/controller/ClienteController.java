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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/clientes")
@Tag(name = "Cliente Controller", description = "Gestion distribuida de Clientes con HATEOAS")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteModelAssembler assembler;

    @Operation(summary = "Listar todos los clientes")
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

    @Operation(summary = "Buscar cliente por ID")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClienteDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            ClienteDTO dto = clienteService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Buscar clientes mediante el ID numerico de un libro")
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

    @Operation(summary = "Buscar cliente por ID de prestamo")
    @GetMapping(value = "/prestamo/{idPrestamo}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ClienteDTO>> buscarPorIdPrestamo(@PathVariable Integer idPrestamo) {
        try {
            ClienteDTO dto = clienteService.buscarPorIdPrestamo(idPrestamo);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear un nuevo cliente")
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

    @Operation(summary = "Actualizar un cliente por ID")
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

    @Operation(summary = "Eliminar un cliente")
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