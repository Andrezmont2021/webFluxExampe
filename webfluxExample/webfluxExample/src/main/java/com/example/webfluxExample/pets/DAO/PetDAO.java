package com.example.webfluxExample.pets.DAO;

import com.example.webfluxExample.pets.model.Pet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetDAO extends ReactiveCrudRepository<Pet, Integer> {

}
