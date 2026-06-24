package com.cyboul.demo.logic.service;

import com.cyboul.demo.exception.PetNotFoundException;
import com.cyboul.demo.logic.data.PetRepository;
import com.cyboul.demo.model.pet.Pet;
import com.cyboul.demo.dto.PetDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PetService {

    private final PetRepository repository;

    public PetService(PetRepository repository) {
        this.repository = repository;
    }

    public List<PetDTO> findAll() {
        return repository.findAll().stream().map(PetDTO::from).toList();
    }

    public PetDTO findById(Long id) {
        return PetDTO.from(getEntity(id));
    }

    public PetDTO create(PetDTO dto) {
        Pet pet = new Pet(null, dto.name(), dto.description(), dto.type());
        return PetDTO.from(repository.save(pet));
    }

    @Transactional
    public void update(Long id, PetDTO dto) {
        Pet existing = getEntity(id);
        existing.setName(dto.name());
        existing.setDescription(dto.description());
        existing.setType(dto.type());
        repository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new PetNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public List<Pet> findAllEntities() {
        return repository.findAll();
    }

    public Pet getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new PetNotFoundException(id));
    }
}
