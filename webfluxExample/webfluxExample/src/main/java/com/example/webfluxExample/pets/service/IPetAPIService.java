package com.example.webfluxExample.pets.service;

import com.example.webfluxExample.utils.POJO.PetBreedInformation;
import com.example.webfluxExample.utils.POJO.PetDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface IPetAPIService {

    Flux<PetBreedInformation> getCatBreeds();

    Flux<PetDTO> getPets();

    Mono<PetDTO> createPet(PetDTO pet);

    Mono<PetDTO> getPetById(int petId);

}
