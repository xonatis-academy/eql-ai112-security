package fr.eql.akatz.demo.petpal.spring.front.entity;

import java.time.LocalDate;

public abstract class Pet {

    protected Long id;
    private String name;
    private LocalDate birthDate;
    private String picture;
    private Owner owner;

    public Pet() {

    }

    public Pet(String name, LocalDate birthDate, String picture, Owner owner) {
        this.name = name;
        this.birthDate = birthDate;
        this.picture = picture;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public LocalDate getBirthDate() {
        return birthDate;
    }
    public String getPicture() {
        return picture;
    }
}
