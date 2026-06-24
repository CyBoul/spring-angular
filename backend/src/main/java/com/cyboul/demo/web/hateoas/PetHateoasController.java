package com.cyboul.demo.web.hateoas;

import com.cyboul.demo.exception.PetNotFoundException;
import com.cyboul.demo.logic.data.PetRepository;
import com.cyboul.demo.logic.service.PetService;
import com.cyboul.demo.model.pet.Pet;
import com.cyboul.demo.dto.PetDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
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
@SecurityRequirement(name = "bearer")
public class PetHateoasController {

    private final PetService petService;
    private final PetRepository petRepository;
    private final PetModelAssembler assembler;

    public PetHateoasController(PetService petService, PetRepository petRepository,
                                PetModelAssembler assembler) {
        this.petService = petService;
        this.petRepository = petRepository;
        this.assembler = assembler;
    }

    @GetMapping("")
    public List<EntityModel<Pet>> viewAll() {
        return petRepository.findAll().stream().map(assembler::toModel).toList();
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public PetDTO create(@Valid @RequestBody PetDTO dto) {
        return petService.create(dto);
    }

    @GetMapping("/{id}")
    public EntityModel<Pet> viewOne(@PathVariable Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(() -> new PetNotFoundException(id));
        return assembler.toModel(pet);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @Valid @RequestBody PetDTO dto) {
        petService.update(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        petService.delete(id);
    }
}
