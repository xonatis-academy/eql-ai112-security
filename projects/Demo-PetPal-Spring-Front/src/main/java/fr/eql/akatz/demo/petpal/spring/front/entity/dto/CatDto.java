package fr.eql.akatz.demo.petpal.spring.front.entity.dto;

import fr.eql.akatz.demo.petpal.spring.front.entity.CatBreed;

import java.time.LocalDate;

public class CatDto {

    private final String name;
    private final LocalDate birthDate;
    private final String picture;
    private final CatBreed breed;
    private final long ownerId;

    public CatDto(String name, LocalDate birthDate, String picture, CatBreed breed, long ownerId) {
        this.name = name;
        this.birthDate = birthDate;
        this.picture = picture;
        this.breed = breed;
        this.ownerId = ownerId;
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
    public CatBreed getBreed() {
        return breed;
    }
    public long getOwnerId() {
        return ownerId;
    }
}
