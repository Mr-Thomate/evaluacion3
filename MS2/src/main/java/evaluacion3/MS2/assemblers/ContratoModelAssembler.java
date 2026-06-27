package evaluacion3.MS2.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import evaluacion3.MS2.controller.ContratoController;
import evaluacion3.MS2.dto.ContratoDTO;

@Component
public class ContratoModelAssembler implements RepresentationModelAssembler<ContratoDTO, EntityModel<ContratoDTO>> {
    @Override
    public EntityModel<ContratoDTO> toModel(ContratoDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(ContratoController.class).obtenerPorId(dto.getIdContrato())).withSelfRel(),
                linkTo(methodOn(ContratoController.class).obtenerTodosContratos()).withRel("todos-los-contratos")
        );
    }
}