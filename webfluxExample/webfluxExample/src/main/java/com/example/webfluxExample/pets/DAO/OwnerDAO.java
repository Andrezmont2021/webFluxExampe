package com.example.webfluxExample.pets.DAO;

import com.example.webfluxExample.pets.model.Owner;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerDAO extends ReactiveCrudRepository<Owner, Integer> {

}
