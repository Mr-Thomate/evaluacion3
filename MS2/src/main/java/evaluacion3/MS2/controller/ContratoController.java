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

import evaluacion3.MS2.assemblers.ContratoModelAssembler;
import evaluacion3.MS2.dto.ContratoDTO;
import evaluacion3.MS2.model.Contrato;
import evaluacion3.MS2.service.ContratoService;

@RestController
@RequestMapping("/api/v1/contratos")
public class ContratoController {
    
    @Autowired
    private ContratoService contratoService;

    @Autowired
    private ContratoModelAssembler assembler;

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

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ContratoDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            ContratoDTO dto = contratoService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

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