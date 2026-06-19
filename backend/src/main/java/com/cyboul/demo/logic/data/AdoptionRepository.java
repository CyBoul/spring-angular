package com.cyboul.demo.logic.data;

import com.cyboul.demo.model.Adoption;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptionRepository extends ListCrudRepository<Adoption, Long> {
    List<Adoption> findByUserId(Long userId);
}
