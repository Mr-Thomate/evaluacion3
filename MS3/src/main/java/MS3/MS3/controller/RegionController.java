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
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/regiones")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @Autowired
    private RegionModelAssembler assembler;

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

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<RegionDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            RegionDTO dto = regionService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

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