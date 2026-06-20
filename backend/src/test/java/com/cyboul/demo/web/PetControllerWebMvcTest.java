package com.cyboul.demo.web;

import com.cyboul.demo.config.SecurityConfig;
import com.cyboul.demo.config.TestUsersConfig;
import com.cyboul.demo.exception.PetNotFoundException;
import com.cyboul.demo.logic.service.JwtService;
import com.cyboul.demo.logic.service.PetService;
import com.cyboul.demo.model.pet.Animal;
import com.cyboul.demo.model.pet.Pet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Slice MVC
 * - Partial Integration Test (without DB, Repo, JPA)
 * - Test controller behavior only
 * - Enable minimal WebSecurity (simulate auth = @WithMockUser)
 * - Simulate users
 */

@WebMvcTest(PetController.class)
@Import({ SecurityConfig.class, TestUsersConfig.class })
@AutoConfigureMockMvc()
public class PetControllerWebMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PetService petService;

    @MockitoBean
    JwtService jwtService;

    @Test
    @WithMockUser(username = "user@mail.com", roles = {"USER"})
    void viewAllPets_shouldReturnOk_whenAuthenticated() throws Exception {

        List<Pet> pets = new ArrayList<>();
        pets.add(new Pet(1L, "Milo", "", Animal.DOG));
        pets.add(new Pet(2L, "Salem", "", Animal.CAT));

        when(petService.findAll()).thenReturn(pets);

        mockMvc.perform(get("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(pets.size()))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Milo"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Salem"));

        verify(petService, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "user@mail.com", roles = {"USER"})
    void viewOnePetById_shouldReturnsOk_whenAuthenticatedAndPetExists() throws Exception {

        Pet pet = new Pet(1L, "Milo", "", Animal.DOG);

        when(petService.findById(1L)).thenReturn(pet);

        mockMvc.perform(get("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Milo"));

        verify(petService, times(1)).findById(1L);
    }

    @Test
    @WithMockUser(username = "user@mail.com", roles = {"USER"})
    void viewOnePetById_shouldReturnsNotFound_whenAuthenticatedAndPetDoesNotExist() throws Exception {

        when(petService.findById(any(Long.class))).thenThrow(new PetNotFoundException(99999L));

        mockMvc.perform(get("/api/pets/99999")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());

        verify(petService, times(1)).findById(99999L);
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN"})
    void createPet_shouldReturnCreated_whenAuthenticatedAsAdmin() throws Exception {

        String name = "Milo";
        String desc = "";
        Animal type = Animal.DOG;

        Pet pet = new Pet();
        pet.setName(name);
        pet.setType(type);

        when(petService.create(any(Pet.class))).thenReturn(pet);

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

        verify(petService, times(1)).create(any(Pet.class));
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN"})
    void updatePet_shouldReturnsNoContent_whenAuthenticatedAsAdminAndPetExists() throws Exception {

        String name = "Milo";
        String desc = "";
        Animal type = Animal.DOG;

        doNothing().when(petService).update(eq(1L), any(Pet.class));

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

        verify(petService, times(1)).update(eq(1L), any(Pet.class));
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN"})
    void updatePet_shouldReturnsNotFound_whenAuthenticatedAsAdminAndPetDoesNotExist() throws Exception {

        String name = "Milo";
        String desc = "";
        Animal type = Animal.DOG;

        doThrow(new PetNotFoundException(1L)).when(petService).update(eq(1L), any(Pet.class));

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

        verify(petService, times(1)).update(eq(1L), any(Pet.class));
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN"})
    void deletePet_shouldReturnsNoContent_whenAuthenticatedAsAdminAndPetExists() throws Exception {

        doNothing().when(petService).delete(any(Long.class));

        mockMvc.perform(delete("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNoContent());

        verify(petService, times(1)).delete(1L);
    }

    @Test
    @WithMockUser(username = "admin@mail.com", roles = {"ADMIN"})
    void deletePet_shouldReturnsNotFound_whenAuthenticatedAsAdminAndPetDoesNotExist() throws Exception {

        doThrow(new PetNotFoundException(99999L)).when(petService).delete(any(Long.class));

        mockMvc.perform(delete("/api/pets/99999")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());

        verify(petService, times(1)).delete(99999L);
    }

    // 403 Forbidden

    @Test
    @WithMockUser(username = "user@mail.com", roles = {"USER"})
    void createPet_shouldReturnForbidden_whenAuthenticatedAsUser() throws Exception {

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"name":"Hacked","type":"DOG"}
                            """))

                .andExpect(status().isForbidden());

        verify(petService, never()).create(any());
    }

    @Test
    @WithMockUser(username = "user@mail.com", roles = {"USER"})
    void updatePet_shouldReturnForbidden_whenAuthenticatedAsUser() throws Exception {

        mockMvc.perform(put("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"name":"Hacked","type":"DOG"}
                            """))

                .andExpect(status().isForbidden());

        verify(petService, never()).update(any(), any());
    }

    @Test
    @WithMockUser(username = "user@mail.com", roles = {"USER"})
    void deletePet_shouldReturnsForbidden_whenNotAuthenticatedAsAdmin() throws Exception {

        mockMvc.perform(delete("/api/pets/1")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isForbidden());

        verify(petService, never()).delete(any());
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
