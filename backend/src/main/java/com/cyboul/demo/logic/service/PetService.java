package com.cyboul.demo.logic.service;

import com.cyboul.demo.exception.PetNotFoundException;
import com.cyboul.demo.logic.data.PetRepository;
import com.cyboul.demo.model.pet.Pet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRepository repository;

    public PetService(PetRepository repository) {
        this.repository = repository;
    }

    public List<Pet> findAll() {
        return repository.findAll();
    }

    public Pet findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new PetNotFoundException(id));
    }

    public Pet create(Pet pet) {
        return repository.save(pet);
    }

    public void update(Long id, Pet pet) {
        repository.findById(id)
                .map(existing -> {
                    pet.setId(existing.getId());
                    return repository.save(pet);
                })
                .orElseThrow(() -> new PetNotFoundException(id));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new PetNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
