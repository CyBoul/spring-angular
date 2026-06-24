package com.cyboul.demo.dto;

import com.cyboul.demo.model.pet.Animal;
import com.cyboul.demo.model.pet.Pet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PetDTO(
        Long id,
        @NotBlank @Size(max = 50) String name,
        @Size(max = 300) String description,
        @NotNull Animal type
) {
    public static PetDTO from(Pet pet) {
        return new PetDTO(pet.getId(), pet.getName(), pet.getDescription(), pet.getType());
    }
}
