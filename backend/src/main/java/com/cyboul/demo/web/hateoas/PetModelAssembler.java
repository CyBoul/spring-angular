package com.cyboul.demo.web.hateoas;

import com.cyboul.demo.model.pet.Pet;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PetModelAssembler {

    public Pet toModel(Pet pet){

        pet.add(linkTo(methodOn(PetHateoasController.class)
                .viewOne(pet.getId()))
                .withSelfRel());

        pet.add(linkTo(methodOn(PetHateoasController.class)
                .viewAll())
                .withRel("all"));

        return pet;
    }
}
