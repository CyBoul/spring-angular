package com.cyboul.demo.logic.disabled;

import com.cyboul.demo.model.pet.Pet;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

/**
 * Some JdbcClient usage
 */
// DISABLED
//@Repository
public class JdbcPetRepository {

    private final JdbcClient jdbcClient;

    public JdbcPetRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    public List<Pet> findAll(){
        return jdbcClient.sql("select * from PET ")
                .query(Pet.class)
                .list();
    }

    public Optional<Pet> findById(Long id){
        return jdbcClient.sql("select * from PET where id = :id ")
                .param("id", id)
                .query(Pet.class)
                .optional();
    }

    public void create(Pet pet){
        var created = jdbcClient
                .sql("INSERT INTO PET(id,name,description,type) VALUES (:id, :name, :description, :type)")
                .param("id", pet.getId())
                .param("name", pet.getName())
                .param("description", pet.getDescription())
                .param("type", pet.getType().toString())
                .update();

        Assert.state(created == 1, "Failed to create Pet");
    }

    public void update(Pet pet, Long id){
        var updated = jdbcClient.sql("UPDATE FROM PET set name = ?, description = :desc, type = :type WHERE id = :id ) ")
                .param("id", id)
                .param("name", pet.getName())
                .param("desc", pet.getDescription())
                .param("type", pet.getType().toString())
                .update();

        Assert.state(updated == 1, "Failed to edit Pet id:" + id);
    }

    public void delete(Long id){
        var deleted = jdbcClient.sql("DELETE FROM PET WHERE id = :id ")
                .param("id", id)
                .update();

        Assert.state(deleted == 1, "Failed to delete Pet id:" + id);
    }

    public List<Pet> findByType(String type){
        return jdbcClient.sql("select * from PET where type = :type ")
                .param("type", type)
                .query(Pet.class)
                .list();
    }

    public int count(){
        return jdbcClient.sql("SELECT * FROM PET")
                .query()
                .listOfRows()
                .size();
    }

    public void createAll(List<Pet> pets){
        pets.forEach(this::create);
    }

    public void saveAll(List<Pet> pets){
        createAll(pets);
    }

}
