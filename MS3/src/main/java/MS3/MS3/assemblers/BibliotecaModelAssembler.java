package MS3.MS3.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import MS3.MS3.controller.BibliotecaController;
import MS3.MS3.dto.BibliotecaDTO;

@Component
public class BibliotecaModelAssembler implements RepresentationModelAssembler<BibliotecaDTO, EntityModel<BibliotecaDTO>> {
    @Override
    public EntityModel<BibliotecaDTO> toModel(BibliotecaDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(BibliotecaController.class).obtenerPorId(dto.getId())).withSelfRel(),
                linkTo(methodOn(BibliotecaController.class).obtenerTodas()).withRel("todas-las-bibliotecas")
        );
    }
}