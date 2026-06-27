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
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/bibliotecas")
public class BibliotecaController {

    @Autowired
    private BibliotecaService bibliotecaService;

    @Autowired
    private BibliotecaModelAssembler assembler;

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

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<BibliotecaDTO>> obtenerPorId(@PathVariable Integer id) {
        BibliotecaDTO dto = bibliotecaService.buscarPorId(id);
        if (dto != null) {
            return ResponseEntity.ok(assembler.toModel(dto));
        }
        return ResponseEntity.notFound().build();
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarBiblioteca(@PathVariable Integer id) {
        String mensaje = bibliotecaService.eliminar(id);
        if (mensaje.startsWith("Error")) {
            return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mensaje, HttpStatus.OK);
    }

    @GetMapping(value = "/prestamo/{idPrestamo}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<BibliotecaDTO>> obtenerPorPrestamo(@PathVariable Integer idPrestamo) {
        try {
            BibliotecaDTO dto = bibliotecaService.buscarPorIdPrestamo(idPrestamo);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

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