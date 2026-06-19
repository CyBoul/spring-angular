package com.cyboul.demo.web;

import com.cyboul.demo.logic.service.AdoptionService;
import com.cyboul.demo.model.Adoption;
import com.cyboul.demo.model.AdoptionRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adoptions")
public class AdoptionController {

    private final AdoptionService adoptionService;

    public AdoptionController(AdoptionService adoptionService) {
        this.adoptionService = adoptionService;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Adoption adopt(@Valid @RequestBody AdoptionRequest request, Authentication auth) {
        return adoptionService.adopt(request.petId(), auth.getName());
    }

    @GetMapping("/my")
    public List<Adoption> myAdoptions(Authentication auth) {
        return adoptionService.findByUserEmail(auth.getName());
    }
}
