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

import Evaluacion3.MS1.assemblers.PrestamoModelAssembler;
import Evaluacion3.MS1.dto.PrestamoDTO;
import Evaluacion3.MS1.model.Prestamo;
import Evaluacion3.MS1.service.PrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/prestamos")
@Tag(name = "Prestamo Controller", description = "Orquestador distribuido de Préstamos con HATEOAS")
public class PrestamoController {
    
    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private PrestamoModelAssembler assembler;

    @Operation(summary = "Obtener todos los prestamos")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<PrestamoDTO>>> obtenerTodosPrestamos() {
        List<EntityModel<PrestamoDTO>> prestamos = prestamoService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (prestamos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(CollectionModel.of(
                prestamos,
                linkTo(methodOn(PrestamoController.class).obtenerTodosPrestamos()).withSelfRel()
        ));
    }

    @Operation(summary = "Buscar prestamo por su ID")
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PrestamoDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            PrestamoDTO dto = prestamoService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Generar un nuevo registro de prestamo con tipo DATE")
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PrestamoDTO>> agregarPrestamo(@Valid @RequestBody Prestamo prestamo) {
        try {
            Prestamo guardado = prestamoService.guardar(prestamo);
            PrestamoDTO dtoCreado = prestamoService.buscarPorId(guardado.getId());
            
            return ResponseEntity
                    .created(linkTo(methodOn(PrestamoController.class).buscarPorId(dtoCreado.getIdPrestamo())).toUri())
                    .body(assembler.toModel(dtoCreado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar un prestamo por ID")
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PrestamoDTO>> actualizarPrestamo(@PathVariable Integer id, @Valid @RequestBody Prestamo prestamo) {
        try {
            Prestamo editado = prestamoService.actualizar(id, prestamo);
            PrestamoDTO dtoActualizado = prestamoService.buscarPorId(editado.getId());
            
            return ResponseEntity.ok(assembler.toModel(dtoActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un prestamo por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPrestamo(@PathVariable Integer id) {
        String resultado = prestamoService.eliminar(id);
        if (resultado.contains("exito")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}