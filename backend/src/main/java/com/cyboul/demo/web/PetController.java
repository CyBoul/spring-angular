package com.cyboul.demo.web;

import com.cyboul.demo.logic.service.PetService;
import com.cyboul.demo.model.pet.Pet;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("")
    public List<Pet> viewAll() {
        return petService.findAll();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Pet create(@Valid @RequestBody Pet pet) {
        return petService.create(pet);
    }

    @GetMapping("/{id}")
    public Pet viewOne(@PathVariable Long id) {
        return petService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody Pet pet) {
        petService.update(id, pet);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        petService.delete(id);
    }
}
