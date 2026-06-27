package MS4.MS4.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import MS4.MS4.controller.EditorialController;
import MS4.MS4.dto.EditorialDTO;

@Component
public class EditorialModelAssembler implements RepresentationModelAssembler<EditorialDTO, EntityModel<EditorialDTO>> {
    @Override
    public EntityModel<EditorialDTO> toModel(EditorialDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(EditorialController.class).obtenerPorId(dto.getIdEditorial())).withSelfRel(),
                linkTo(methodOn(EditorialController.class).obtenerTodas()).withRel("todas-las-editoriales")
        );
    }
}