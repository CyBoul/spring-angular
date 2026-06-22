package com.cyboul.demo.dto;

import com.cyboul.demo.model.Adoption;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record AdoptionDTO(
        Long id,
        @NotNull @Positive Long petId,
        Long userId,
        LocalDateTime creationTime
) {
    public static AdoptionDTO from(Adoption adoption) {
        return new AdoptionDTO(
                adoption.getId(), adoption.getPetId(),
                adoption.getUserId(), adoption.getCreationTime());
    }
}
