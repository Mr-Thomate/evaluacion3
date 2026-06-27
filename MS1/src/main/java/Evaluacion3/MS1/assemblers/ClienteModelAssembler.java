package Evaluacion3.MS1.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import Evaluacion3.MS1.controller.ClienteController;
import Evaluacion3.MS1.dto.ClienteDTO;

@Component
public class ClienteModelAssembler implements RepresentationModelAssembler<ClienteDTO, EntityModel<ClienteDTO>> {

    @Override
    public EntityModel<ClienteDTO> toModel(ClienteDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(ClienteController.class).obtenerPorId(dto.getId())).withSelfRel(),
                linkTo(methodOn(ClienteController.class).obtenerTodos()).withRel("todos-los-clientes")
        );
    }
}