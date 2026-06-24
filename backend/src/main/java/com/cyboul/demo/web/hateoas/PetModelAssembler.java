package com.cyboul.demo.web.hateoas;

import com.cyboul.demo.model.pet.Pet;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PetModelAssembler {

    public EntityModel<Pet> toModel(Pet pet) {
        return EntityModel.of(pet,
                linkTo(methodOn(PetHateoasController.class).viewOne(pet.getId())).withSelfRel(),
                linkTo(methodOn(PetHateoasController.class).viewAll()).withRel("all"));
    }
}
