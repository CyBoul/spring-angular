package com.cyboul.demo.dto;

import jakarta.validation.constraints.NotEmpty;

public record AuthDTO(
        @NotEmpty String email,
        @NotEmpty String password
) {}
