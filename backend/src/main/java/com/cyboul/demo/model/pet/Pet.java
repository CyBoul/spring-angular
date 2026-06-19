package com.cyboul.demo.model.pet;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Entity
public class Pet extends RepresentationModel<Pet> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;

    @Size(max = 300)
    private String description;

    @Enumerated(EnumType.STRING)
    private Animal type;

    public Pet (){
        this.name = "";
        this.description = "";
        this.type = Animal.UNKNOWN;
    }

    public Pet (String name){
        this.name = name;
        this.description = "";
        this.type = Animal.UNKNOWN;
    }

    @JsonCreator
    public Pet (Long id, String name, String description, Animal type){
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
    }

}
