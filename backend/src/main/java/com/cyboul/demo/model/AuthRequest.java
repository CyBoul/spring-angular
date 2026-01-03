package com.cyboul.demo.model;

import jakarta.validation.constraints.NotEmpty;

public record AuthRequest(
        @NotEmpty String email,
        @NotEmpty String password
){}