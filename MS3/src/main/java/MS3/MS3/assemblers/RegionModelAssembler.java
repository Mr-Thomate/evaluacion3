package MS3.MS3.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import MS3.MS3.controller.RegionController;
import MS3.MS3.dto.RegionDTO;

@Component
public class RegionModelAssembler implements RepresentationModelAssembler<RegionDTO, EntityModel<RegionDTO>> {
    @Override
    public EntityModel<RegionDTO> toModel(RegionDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(RegionController.class).obtenerPorId(dto.getIdRegion())).withSelfRel(),
                linkTo(methodOn(RegionController.class).obtenerTodas()).withRel("todas-las-regiones")
        );
    }
}