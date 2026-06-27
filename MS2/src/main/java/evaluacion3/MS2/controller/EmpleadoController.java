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

@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private EmpleadoModelAssembler assembler;

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

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<EmpleadoDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            EmpleadoDTO dto = empleadoService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

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