package com.cyboul.demo.logic.exec;

import com.cyboul.demo.logic.data.PetRepository;
import com.cyboul.demo.logic.data.UserRepository;
import com.cyboul.demo.model.pet.Pets;
import com.cyboul.demo.model.user.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

/**
 * Load JSON data after application has booted
 * /resources/data/Pets.json
 * /resources/data/Users.json
 *
 */
@Component
@Profile("dev && !test")
public class JsonDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(JsonDataLoader.class);

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final ObjectMapper objMapper;
    private final PasswordEncoder encoder;

    @Value("classpath:data/Pets.json")
    private Resource petsResource;

    @Value("classpath:data/Users.json")
    private Resource usersResource;

    public JsonDataLoader(UserRepository userRepository, PetRepository petRepository, ObjectMapper objMapper, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.objMapper = objMapper;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        createPets();
        createUsers();
    }

    private void createUsers() {
        if( userRepository.findAll().isEmpty() ) {
            try (InputStream is = usersResource.getInputStream()) {
                Users users = objMapper.readValue(is, Users.class);
                users.users().forEach(u -> {
                    String raw = u.getPassword();
                    u.setPassword(encoder.encode(raw));
                    //log.info("encoding: " + raw + " as: " + u.getPassword());
                });
                log.info("Reading and injecting {} users from JSON into the database", users.users().size());
                userRepository.saveAll(users.users());

            } catch (IOException e) {
                throw new RuntimeException("Failed to load JSON Users data", e);
            }
        } else {
            log.info("Users Data already stored !");
        }
    }

    private void createPets() {
        if( petRepository.findAll().isEmpty() ){
            try (InputStream is = petsResource.getInputStream()) {
                Pets pets = objMapper.readValue(is, Pets.class);
                log.info("Reading and injecting {} pets from JSON into the database", pets.pets().size());
                petRepository.saveAll(pets.pets());

            } catch (IOException e) {
                throw new RuntimeException("Failed to load JSON Pets data", e);
            }
        } else {
            log.info("Pets Data already stored !");
        }
    }
}
