package com.example.webfluxExample.pets.service;

import com.example.webfluxExample.pets.DAO.OwnerDAO;
import com.example.webfluxExample.pets.DAO.PetDAO;
import com.example.webfluxExample.pets.model.Owner;
import com.example.webfluxExample.pets.model.Pet;
import com.example.webfluxExample.utils.Converters;
import com.example.webfluxExample.utils.POJO.PetBreedInformation;
import com.example.webfluxExample.utils.POJO.PetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;


@Service
public class PetAPIServiceImpl implements IPetAPIService {

    @Autowired
    PetDAO petDAO;

    @Autowired
    OwnerDAO ownerDAO;


    @Override
    public Flux<PetBreedInformation> getCatBreeds() {
        WebClient client = this.establishConnection();

        // Get mono element (0 or 1) and convert to hashmap
        Mono<HashMap> info = client.get().uri("https://catfact.ninja/breeds").retrieve().bodyToMono(HashMap.class);

        //Get only the data property, cast to collection
        Flux data = info.map(property -> property.get("data")).cast(ArrayList.class).flatMapIterable(breedList -> breedList);

        // Return the collection of PetInformation POJO information
        return Converters.convertFluxPetGenericToBreedInformation(data);
    }

    @Override
    public Flux<PetDTO> getPets() {
        Flux<Pet> pets = Flux.empty();
        try {
            pets = petDAO.findAll().flatMap(this::setOwners);
        } catch (Exception e) {
            System.out.println("Error when try to get the pets");
        }

        return Converters.convertFluxPetToDTO(pets);
    }

    private Mono<Pet> setOwners(Pet pet) {
        try {
            return ownerDAO.findById(pet.getOwnerId()).map(owner -> {
                pet.setOwner(owner);
                return pet;
            });
        } catch (Exception e) {
            System.out.printf("Error when try to get the owners associated to the pet: %s%n", pet.getName());
            return Mono.empty();
        }
    }


    @Override
    public Mono<PetDTO> createPet(PetDTO pet) {
        // Convert from DTO's to models (Pet, Owner)
        Owner owner = new Owner();
        Pet petModel = new Pet();
        try {
            owner = Converters.convertOwnerDTOToOwner(pet.getOwner());
            petModel = Converters.convertPetDTOToPet(pet);
        } catch (Exception e) {
            System.out.println("Error when try to convert DTo's to models (Pet, Owner)");
        }

        // First, we need to register the owner (createOwner)
        Pet finalPetModel = petModel;
        Mono<Pet> petCreated = Mono.empty();
        try {
            petCreated = createOwner(owner).flatMap(ownerFound -> {
                finalPetModel.setOwnerId(ownerFound.getId());
                finalPetModel.setOwner(ownerFound);
                return petDAO.save(finalPetModel);
            });
        } catch (Exception e) {
            System.out.println("Error when try to create the pet");
        }

        return Converters.convertMonoPetToDTO(petCreated);
    }

    private Mono<Owner> createOwner(Owner owner) {
        try {
            if (owner.getId() != 0) {
                // Owner with id (maybe can exist on db)
                ownerDAO.findById(owner.getId()).map(result -> {
                    if (result != null) {
                        return Mono.just(result);
                    } else {
                        return Mono.empty();
                    }
                });
            } else {
                // Owner without id, Register new Owner
                return ownerDAO.save(owner);
            }
        } catch (Exception e) {
            System.out.println("Error when try to create the owner");
        }
        return Mono.empty();
    }

    @Override
    public Mono<PetDTO> getPetById(int petId) {
        Mono<Pet> pet = Mono.empty();
        try {
            pet = petDAO.findById(petId).flatMap(element -> setOwners(element));
        } catch (Exception e) {
            System.out.println("Error when try to get the pet by id");
        }
        return Converters.convertMonoPetToDTO(pet);
    }

    private WebClient establishConnection() {
        return WebClient.create();
    }
}
