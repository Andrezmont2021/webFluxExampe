package com.example.webfluxExample.pets.controller;

import com.example.webfluxExample.pets.service.PetAPIServiceImpl;
import com.example.webfluxExample.utils.POJO.PetBreedInformation;
import com.example.webfluxExample.utils.POJO.PetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/petsAPI")
public class PetAPIController {

    @Autowired
    private PetAPIServiceImpl petApiService;


    @GetMapping("/catBreeds")
    public ResponseEntity<Flux<PetBreedInformation>> getCatBreeds() {
        return new ResponseEntity<>(petApiService.getCatBreeds(), HttpStatus.OK);
    }

    @GetMapping("/pets")
    public ResponseEntity<Flux<PetDTO>> getPets() {
        return new ResponseEntity<>(petApiService.getPets(), HttpStatus.OK);
    }

    @GetMapping("/pets/{petId}")
    public ResponseEntity<Mono<PetDTO>> getPetById(@PathVariable int petId) {
        return new ResponseEntity<>(petApiService.getPetById(petId), HttpStatus.OK);
    }

    @PostMapping("/pets/create")
    public ResponseEntity<Mono<PetDTO>> createPet(@RequestBody PetDTO pet) {
        return new ResponseEntity<>(petApiService.createPet(pet), HttpStatus.OK);
    }

}
