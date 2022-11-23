package com.example.webfluxExample.pets.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("pet")
public class Pet {
    @Id
    @Column("id")
    private int id;

    @Column("name")
    private String name;

    @Column("breed")
    private String breed;

    @Column("owner_id")
    private int ownerId;

    @Transient
    private Owner owner;

}
