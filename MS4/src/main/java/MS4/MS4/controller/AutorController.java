package MS4.MS4.controller;

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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MS4.MS4.assemblers.AutorModelAssembler;
import MS4.MS4.dto.AutorDTO;
import MS4.MS4.model.Autor;
import MS4.MS4.service.AutorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/autores")
public class AutorController {
    
    @Autowired
    private AutorService autorService;

    @Autowired
    private AutorModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<AutorDTO>>> obtenerTodosAutores() {
        List<EntityModel<AutorDTO>> autores = autorService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (autores.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(CollectionModel.of(
                autores,
                linkTo(methodOn(AutorController.class).obtenerTodosAutores()).withSelfRel()
        ));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AutorDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            AutorDTO dto = autorService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/libro/{titulo}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<AutorDTO>>> obtenerAutorPorTituloLibro(@PathVariable String titulo) {
        try {
            List<EntityModel<AutorDTO>> autores = autorService.buscarPorTituloLibro(titulo).stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());
                    
            if(autores.isEmpty()) return ResponseEntity.noContent().build();
            
            return ResponseEntity.ok(CollectionModel.of(
                    autores,
                    linkTo(methodOn(AutorController.class).obtenerAutorPorTituloLibro(titulo)).withSelfRel()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AutorDTO>> guardarAutor(@Valid @RequestBody Autor autor) {
        try {
            Autor guardado = autorService.guardar(autor);
            AutorDTO dtoCreado = autorService.buscarPorId(guardado.getId());
            return ResponseEntity
                    .created(linkTo(methodOn(AutorController.class).obtenerPorId(dtoCreado.getId())).toUri())
                    .body(assembler.toModel(dtoCreado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AutorDTO>> editarAutor(@PathVariable Integer id, @Valid @RequestBody Autor autor) {
        try {
            Autor editado = autorService.guardar(autor);
            AutorDTO dtoEditado = autorService.buscarPorId(editado.getId());
            return ResponseEntity.ok(assembler.toModel(dtoEditado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<AutorDTO>> actualizarAutor(@PathVariable Integer id, @Valid @RequestBody Autor autor) {
        try {
            Autor actualizado = autorService.actualizar(id, autor);
            AutorDTO dtoActualizado = autorService.buscarPorId(actualizado.getId());
            return ResponseEntity.ok(assembler.toModel(dtoActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarAutor(@PathVariable Integer id) {
        String resultado = autorService.eliminar(id);
        if (resultado.contains("exito")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}