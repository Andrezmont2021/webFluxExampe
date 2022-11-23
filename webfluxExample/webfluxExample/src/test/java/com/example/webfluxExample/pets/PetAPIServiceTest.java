package com.example.webfluxExample.pets;

import com.example.webfluxExample.pets.DAO.OwnerDAO;
import com.example.webfluxExample.pets.DAO.PetDAO;
import com.example.webfluxExample.pets.model.Owner;
import com.example.webfluxExample.pets.model.Pet;
import com.example.webfluxExample.pets.service.PetAPIServiceImpl;
import com.example.webfluxExample.utils.POJO.OwnerDTO;
import com.example.webfluxExample.utils.POJO.PetDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetAPIServiceTest {

    @InjectMocks
    private PetAPIServiceImpl petAPIService;

    @Mock
    private PetDAO petDAO;

    @Mock
    private OwnerDAO ownerDAO;

    @Mock
    private ObjectMapper objectMapper;

    private Flux<Pet> petsEmpty;
    private Flux<Pet> petsWithValues;

    private Mono<Owner> ownerWithValue;

    private Pet pet1;

    private Pet newPet;
    private PetDTO newPetDTO;
    private Pet newPetWithId;
    private PetDTO newPetWithIdDTO;
    private Pet pet2;
    private Pet pet3;

    private PetDTO petDTO1;
    private PetDTO petDTO2;
    private PetDTO petDTO3;

    private Owner owner1;
    private Owner owner1WithoutId;
    private OwnerDTO owner1WithoutIdDTO;
    private OwnerDTO ownerDTO1;

    @BeforeEach
    private void setup() {
        petsEmpty = Flux.empty();
        pet1 = new Pet(1, "Bruno", "Pastor Aleman", 1, null);
        pet2 = new Pet(2, "Sasha", "Schnauzer", 1, null);
        pet3 = new Pet(3, "Lucas", "Doberman", 1, null);

        petsWithValues = Flux.just(pet1, pet2, pet3);

        owner1 = new Owner(1, "Jose", 15);
        owner1WithoutId = new Owner(0, "Jose", 15);
        owner1WithoutIdDTO = new OwnerDTO(0, "Jose", 15);
        ownerWithValue = Mono.just(owner1);
        ownerDTO1 = new OwnerDTO(1, "Jose", 15);

        newPet = new Pet(0, "Sasha", "Pinscher", 1, owner1);
        newPetDTO = new PetDTO(0, "Sasha", "Pinscher", owner1WithoutIdDTO);
        newPetWithId = new Pet(4, "Sasha", "Pinscher", 1, owner1);
        newPetWithIdDTO = new PetDTO(4, "Sasha", "Pinscher", ownerDTO1);

        petDTO1 = new PetDTO(1, "Bruno", "Pastor Aleman", ownerDTO1);
        petDTO2 = new PetDTO(2, "Sasha", "Schnauzer", ownerDTO1);
        petDTO3 = new PetDTO(3, "Lucas", "Doberman", ownerDTO1);



    }

    @Test
    public void pets_list_not_empty() {

        when(petDAO.findAll()).thenReturn(petsWithValues);
        when(ownerDAO.findById(1)).thenReturn(ownerWithValue);
        lenient().when(objectMapper.convertValue(pet1, PetDTO.class)).thenReturn(petDTO1);
        lenient().when(objectMapper.convertValue(pet2, PetDTO.class)).thenReturn(petDTO2);
        lenient().when(objectMapper.convertValue(pet3, PetDTO.class)).thenReturn(petDTO3);

        Flux<PetDTO> petListToVerify = petAPIService.getPets();

        // Verify that exist 3 pets
        StepVerifier.create(petListToVerify)
                .expectNextCount(3)
                .verifyComplete();
        // Verify that objects are the expected
        StepVerifier.create(petListToVerify)
                .expectNext(petDTO1)
                .expectNext(petDTO2)
                .expectNext(petDTO3)
                .verifyComplete();
        // Verify invocation of findAll (pets)
        verify(petDAO, times(1)).findAll();
    }

    @Test
    public void pets_list_empty() {
        when(petDAO.findAll()).thenReturn(petsEmpty);

        Flux<PetDTO> petListToVerify = petAPIService.getPets();

        // Verify that exist 0 pets
        StepVerifier.create(petListToVerify)
                .expectNextCount(0)
                .verifyComplete();
        // Verify invocation of findAll (pets)
        verify(petDAO, times(1)).findAll();
    }

    @Test
    public void register_pet_with_a_not_existing_owner() {
        lenient().when(objectMapper.convertValue(newPetDTO, Pet.class)).thenReturn(newPet);
        lenient().when(objectMapper.convertValue(owner1WithoutIdDTO, Owner.class)).thenReturn(owner1WithoutId);
        when(ownerDAO.save(owner1WithoutId)).thenReturn(Mono.just(owner1));
        when(petDAO.save(newPet)).thenReturn(Mono.just(newPetWithId));
        lenient().when(objectMapper.convertValue(newPetWithId, PetDTO.class)).thenReturn(newPetWithIdDTO);

        Mono<PetDTO> petRegisteredToVerify = petAPIService.createPet(newPetDTO);

        // Verify that object is the expected
        StepVerifier.create(petRegisteredToVerify)
                .expectNext(newPetWithIdDTO)
                .verifyComplete();
        // Verify invocation of save (pets)
        verify(petDAO, times(1)).save(newPet);
    }

}
