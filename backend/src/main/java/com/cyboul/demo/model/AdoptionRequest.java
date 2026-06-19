package com.cyboul.demo.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AdoptionRequest(
        @NotNull @Positive Long petId
) {}
