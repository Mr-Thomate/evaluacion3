package MS4.MS4.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import MS4.MS4.controller.LibroController;
import MS4.MS4.dto.LibroDTO;

@Component
public class LibroModelAssembler implements RepresentationModelAssembler<LibroDTO, EntityModel<LibroDTO>> {
    @Override
    public EntityModel<LibroDTO> toModel(LibroDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(LibroController.class).obtenerPorId(dto.getIsbn())).withSelfRel(),
                linkTo(methodOn(LibroController.class).obtenerTodosLibros()).withRel("todos-los-libros")
        );
    }
}