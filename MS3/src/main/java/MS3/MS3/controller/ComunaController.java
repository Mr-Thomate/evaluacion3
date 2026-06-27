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
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/comunas")
public class ComunaController {
    
    @Autowired
    private ComunaService comunaService;

    @Autowired
    private ComunaModelAssembler assembler;

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

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ComunaDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            ComunaDTO dto = comunaService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCategoria(@PathVariable Integer id) {
        String resultado = comunaService.eliminar(id);
        if (resultado.contains("exito")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{biblioteca}", produces = MediaTypes.HAL_JSON_VALUE)
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