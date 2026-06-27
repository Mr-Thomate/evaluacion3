package MS3.MS3.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import MS3.MS3.controller.ComunaController;
import MS3.MS3.dto.ComunaDTO;

@Component
public class ComunaModelAssembler implements RepresentationModelAssembler<ComunaDTO, EntityModel<ComunaDTO>> {
    @Override
    public EntityModel<ComunaDTO> toModel(ComunaDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(ComunaController.class).obtenerPorId(dto.getIdComuna())).withSelfRel(),
                linkTo(methodOn(ComunaController.class).obtenerTodasComunas()).withRel("todas-las-comunas")
        );
    }
}