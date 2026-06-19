package com.cyboul.demo.web.hateoas;

import com.cyboul.demo.logic.service.PetService;
import com.cyboul.demo.model.pet.Pet;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * This controller is a small HATEOAS example kept intentionally
 * as a reference pattern.
 *
 * In a production SPA-oriented API, I would likely simplify
 * representations unless workflow complexity justified HATEOAS.
 */

@RestController
@RequestMapping("/api/hateoas/pets")
public class PetHateoasController {

    private final PetService petService;
    private final PetModelAssembler assembler;

    public PetHateoasController(PetService petService, PetModelAssembler assembler) {
        this.petService = petService;
        this.assembler = assembler;
    }

    @GetMapping("")
    public List<Pet> viewAll() {
        return petService.findAll().stream().map(assembler::toModel).toList();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Pet create(@Valid @RequestBody Pet pet) {
        return petService.create(pet);
    }

    @GetMapping("/{id}")
    public Pet viewOne(@PathVariable Long id) {
        return assembler.toModel(petService.findById(id));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody Pet pet) {
        petService.update(id, pet);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        petService.delete(id);
    }
}
