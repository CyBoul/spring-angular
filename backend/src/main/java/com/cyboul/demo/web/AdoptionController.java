package com.cyboul.demo.web;

import com.cyboul.demo.logic.service.AdoptionService;
import com.cyboul.demo.dto.AdoptionDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adoptions")
@SecurityRequirement(name = "bearer")
public class AdoptionController {

    private final AdoptionService adoptionService;

    public AdoptionController(AdoptionService adoptionService) {
        this.adoptionService = adoptionService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public AdoptionDTO adopt(@Valid @RequestBody AdoptionDTO dto, Authentication auth) {
        return adoptionService.adopt(dto.petId(), auth.getName());
    }

    @GetMapping("/my")
    public List<AdoptionDTO> myAdoptions(Authentication auth) {
        return adoptionService.findByUserEmail(auth.getName());
    }
}
