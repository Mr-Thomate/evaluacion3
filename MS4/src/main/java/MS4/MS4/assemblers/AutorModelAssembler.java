package MS4.MS4.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import MS4.MS4.controller.AutorController;
import MS4.MS4.dto.AutorDTO;

@Component
public class AutorModelAssembler implements RepresentationModelAssembler<AutorDTO, EntityModel<AutorDTO>> {
    @Override
    public EntityModel<AutorDTO> toModel(AutorDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(AutorController.class).obtenerPorId(dto.getId())).withSelfRel(),
                linkTo(methodOn(AutorController.class).obtenerTodosAutores()).withRel("todos-los-autores")
        );
    }
}