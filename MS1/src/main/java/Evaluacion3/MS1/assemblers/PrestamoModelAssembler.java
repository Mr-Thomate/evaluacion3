package Evaluacion3.MS1.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import Evaluacion3.MS1.controller.PrestamoController;
import Evaluacion3.MS1.dto.PrestamoDTO;

@Component
public class PrestamoModelAssembler implements RepresentationModelAssembler<PrestamoDTO, EntityModel<PrestamoDTO>> {

    @Override
    public EntityModel<PrestamoDTO> toModel(PrestamoDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(PrestamoController.class).buscarPorId(dto.getIdPrestamo())).withSelfRel(),
                linkTo(methodOn(PrestamoController.class).obtenerTodosPrestamos()).withRel("todos-los-prestamos")
        );
    }
}