package com.example.webfluxExample.pets.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("owner")
public class Owner {
    @Id
    @Column("id")
    private int id;

    @Column("name")
    private String name;

    @Column("age")
    private int age;

}
