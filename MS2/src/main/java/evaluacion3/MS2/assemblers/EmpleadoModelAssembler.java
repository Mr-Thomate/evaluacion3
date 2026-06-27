package evaluacion3.MS2.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import evaluacion3.MS2.controller.EmpleadoController;
import evaluacion3.MS2.dto.EmpleadoDTO;

@Component
public class EmpleadoModelAssembler implements RepresentationModelAssembler<EmpleadoDTO, EntityModel<EmpleadoDTO>> {
    @Override
    public EntityModel<EmpleadoDTO> toModel(EmpleadoDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(EmpleadoController.class).obtenerPorId(dto.getIdEmpleado())).withSelfRel(),
                linkTo(methodOn(EmpleadoController.class).obtenerTodosEmpleados()).withRel("todos-los-empleados")
        );
    }
}