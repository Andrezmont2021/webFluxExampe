package com.example.webfluxExample.utils;

import com.example.webfluxExample.pets.model.Owner;
import com.example.webfluxExample.pets.model.Pet;
import com.example.webfluxExample.utils.POJO.OwnerDTO;
import com.example.webfluxExample.utils.POJO.PetBreedInformation;
import com.example.webfluxExample.utils.POJO.PetDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Converters {
    @Bean
    public static ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    public static Flux<PetDTO> convertFluxPetToDTO(Flux<Pet> pets) {
        return pets.map(element -> objectMapper().convertValue(element, PetDTO.class));
    }

    public static Mono<PetDTO> convertMonoPetToDTO(Mono<Pet> pet) {
        return pet.map(element -> objectMapper().convertValue(element, PetDTO.class));
    }

    public static Flux convertFluxPetGenericToBreedInformation(Flux data) {
        return data.map(element -> objectMapper().convertValue(element, PetBreedInformation.class));
    }

    public static Owner convertOwnerDTOToOwner(OwnerDTO owner) {
        return objectMapper().convertValue(owner, Owner.class);
    }

    public static Pet convertPetDTOToPet(PetDTO pet) {
        return objectMapper().convertValue(pet, Pet.class);
    }

}
