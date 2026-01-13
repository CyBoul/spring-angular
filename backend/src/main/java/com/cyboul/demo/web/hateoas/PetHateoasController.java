package com.cyboul.demo.web.hateoas;

import com.cyboul.demo.logic.data.PetRepository;
import com.cyboul.demo.model.pet.Pet;
import jakarta.validation.Valid;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

    private final PetRepository repository;
    private final PetModelAssembler assembler;

    public PetHateoasController(PetRepository repo, PetModelAssembler assembler){
        this.repository = repo;
        this.assembler = assembler;
    }

    @GetMapping("")
    public ResponseEntity<List<Pet>> viewAll(){
        List<Pet> pets = repository.findAll()
                .stream()
                .map(assembler::toModel)
                .toList();

        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public void create(@Valid @RequestBody Pet pet){
        repository.save(pet);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> viewOne(@PathVariable Long id){
        return repository.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody Pet pet){
        repository.findById(id)
                .map(existing -> {
                    pet.setId(existing.getId());
                    return repository.save(pet);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        if(!repository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }


}
