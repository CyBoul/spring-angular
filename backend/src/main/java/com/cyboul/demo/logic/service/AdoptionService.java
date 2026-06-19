package com.cyboul.demo.logic.service;

import com.cyboul.demo.logic.data.AdoptionRepository;
import com.cyboul.demo.logic.data.UserRepository;
import com.cyboul.demo.model.Adoption;
import com.cyboul.demo.model.user.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final PetService petService;
    private final UserRepository userRepository;

    public AdoptionService(AdoptionRepository adoptionRepository, PetService petService, UserRepository userRepository) {
        this.adoptionRepository = adoptionRepository;
        this.petService = petService;
        this.userRepository = userRepository;
    }

    public Adoption adopt(Long petId, String userEmail) {
        petService.findById(petId); // validates pet exists — throws PetNotFoundException if not
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userEmail));
        return adoptionRepository.save(new Adoption(null, petId, user.getId(), LocalDateTime.now()));
    }

    public List<Adoption> findByUserEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userEmail));
        return adoptionRepository.findByUserId(user.getId());
    }
}
