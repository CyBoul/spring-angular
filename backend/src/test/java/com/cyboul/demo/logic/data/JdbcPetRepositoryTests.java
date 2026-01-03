package com.cyboul.demo.logic.data;

import com.cyboul.demo.logic.disabled.JdbcPetRepository;
import com.cyboul.demo.model.pet.Animal;
import com.cyboul.demo.model.pet.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Profile("test")
//@SpringBootTest /* avoid to load all Spring context */
@JdbcTest
@Import(JdbcPetRepository.class)
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
//@PropertySource("classpath:application-test.properties")
public class JdbcPetRepositoryTests {

    @Autowired
    JdbcPetRepository repository;

    @BeforeEach
    void setup(){
        repository.create(new Pet(
                55L,
                "Goldie",
                "Cute little dragon living in the dream",
                Animal.UNKNOWN));

        repository.create(new Pet(
                85L,
                "PussInBoots",
                "Smart and charming",
                Animal.CAT ));
    }

    @Test
    public void shouldFindTwoPets() {
        assertEquals(2, repository.count());
    }

    @Test
    public void shouldCreateOneMorePet() {
        repository.create(new Pet(
                66L,
                "Belle",
                "Very helpful",
                Animal.DOG ));

        assertEquals(3, repository.count());
    }

    @Test
    public void shouldReturnPussInBoots() {
        var pet = repository.findById(85L);
        assertTrue(pet.isPresent());
        assertEquals("PussInBoots", pet.get().getName());
    }
}
