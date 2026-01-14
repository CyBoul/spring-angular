package com.cyboul.demo.web;

import com.cyboul.demo.config.MvcTestConfig;
import com.cyboul.demo.logic.data.PetRepository;
import com.cyboul.demo.model.pet.Animal;
import com.cyboul.demo.model.pet.Pet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Slice MVC
 * - Partial Integration Test (without DB, Repo, JPA)
 * - Test controller behavior only
 * - Enable minimal WebSecurity (simulate auth = @WithMockUser)
 *   JwtFilter being a @Component, we must exclude it to not interfere
 */

@WebMvcTest(
        controllers = PetController.class,
        excludeFilters = @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = JwtFilter.class
        ))
@Import(MvcTestConfig.class)
public class PetControllerWebMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PetRepository petRepository;

    @Test
    @WithMockUser(username = "user@mail.com", roles = {"USER"})
    void viewAllPets_shouldReturnOk_whenAuthenticated() throws Exception {

        List<Pet> pets = new ArrayList<>();
        pets.add(new Pet(1L, "Milo", "", Animal.DOG));
        pets.add(new Pet(2L, "Salem", "", Animal.CAT));

        when(petRepository.findAll()).thenReturn(pets);

        mockMvc.perform(get("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(pets.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Milo"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Salem"));

        verify(petRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "user@mail.com", roles = {"USER"})
    void viewOnePetById_shouldReturnsOk_whenAuthenticatedAndPetExists() throws Exception {

        Pet pet = new Pet(1L, "Milo", "", Animal.DOG);

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        mockMvc.perform(get("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Milo"));

        verify(petRepository, times(1)).findById(1L);
    }

    @Test
    @WithMockUser(username = "user@mail.com", roles = {"USER"})
    void viewOnePetById_shouldReturnsNotFound_whenAuthenticatedAndPetDoesNotExist() throws Exception {

        when(petRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pets/99999")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());

        verify(petRepository, times(1)).findById(99999L);
    }

    @Test
    @WithMockUser(username = "user@mail.com", roles = {"USER"})
    void createPet_shouldReturnOk_whenAuthenticated() throws Exception {

        long id = 1L;
        String name = "Milo";
        String desc = "";
        Animal type = Animal.DOG;

        Pet pet = new Pet();
        pet.setName(name);
        pet.setType(type);

        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                            {
                                "name": "%s",
                                "description": "%s",
                                "type": "%s"
                            }
                            """, name, desc, type.name())))

                .andExpect(status().isCreated());

        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    @WithMockUser(username = "user@mail.com", roles = {"USER"})
    void updatePet_shouldReturnsNoContent_whenAuthenticatedAndPetExists() throws Exception {

        String name = "Milo";
        String desc = "";
        Animal type = Animal.DOG;

        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName(name);
        pet.setType(type);

        when(petRepository.findById(any(Long.class))).thenReturn(Optional.of(pet));
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        mockMvc.perform(put("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                            {
                                "name": "%s",
                                "description": "%s",
                                "type": "%s"
                            }
                            """, name, desc, type.name())))

                .andExpect(status().isNoContent());

        verify(petRepository, times(1)).findById(1L);
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    @WithMockUser(username = "user@mail.com", roles = {"USER"})
    void updatePet_shouldReturnsNotFound_whenAuthenticatedAndPetDoesNotExist() throws Exception {

        String name = "Milo";
        String desc = "";
        Animal type = Animal.DOG;

        when(petRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                            {
                                "name": "%s",
                                "description": "%s",
                                "type": "%s"
                            }
                            """, name, desc, type.name())))

                .andExpect(status().isNotFound());

        verify(petRepository, times(1)).findById(1L);
        verify(petRepository, never()).save(any());
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN"})
    void deletePet_shouldReturnsNoContent_whenAuthenticatedAsAdminAndPetExists() throws Exception {

        when(petRepository.existsById(any(Long.class))).thenReturn(true);
        doNothing().when(petRepository).deleteById(any(Long.class));

        mockMvc.perform(delete("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNoContent());

        verify(petRepository, times(1)).existsById(1L);
        verify(petRepository, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN"})
    void deletePet_shouldReturnsNotFound_whenAuthenticatedAsAdminAndPetDoesNotExist() throws Exception {

        when(petRepository.existsById(any(Long.class))).thenReturn(false);

        mockMvc.perform(delete("/api/pets/99999")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());

        verify(petRepository, times(1)).existsById(99999L);
        verify(petRepository, never()).deleteById(any());
    }

    // 403 Forbidden

    @Test
    @WithMockUser(username = "user@mail.com", roles = {"USER"})
    void deletePet_shouldReturnsForbidden_whenNotAuthenticatedAsAdmin() throws Exception {

        when(petRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isForbidden());

        verify(petRepository, never()).existsById(1L);
        verify(petRepository, never()).deleteById(any());
    }

    // 401 Unauthorized

    @Test
    void viewAllPets_shouldReturnUnauthorized_whenNotAuthenticated() throws Exception {

        mockMvc.perform(get("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isUnauthorized());
    }

    @Test
    void viewOnePetById_shouldReturnsUnauthorized_whenNotAuthenticated() throws Exception {

        mockMvc.perform(get("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isUnauthorized());
    }

    @Test
    void createPet_shouldReturnUnauthorized_whenNotAuthenticated() throws Exception {

        String name = "Milo";
        String desc = "Some description about the pet";
        Animal type = Animal.DOG;

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                            {
                                "name": "%s",
                                "description": "%s",
                                "type": "%s"
                            }
                            """, name, desc, type.name())))

                .andExpect(status().isUnauthorized());
    }

    @Test
    void updatePet_shouldReturnsUnauthorized_whenNotAuthenticated() throws Exception {

        String name = "Milo";
        String desc = "";
        Animal type = Animal.DOG;

        mockMvc.perform(put("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                            {
                                "name": "%s",
                                "description": "%s",
                                "type": "%s"
                            }
                            """, name, desc, type.name())))

                .andExpect(status().isUnauthorized());
    }

    @Test
    void deletePet_shouldReturnsUnauthorized_whenNotAuthenticated() throws Exception {

        mockMvc.perform(delete("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isUnauthorized());
    }

}
