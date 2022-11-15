package fr.eql.akatz.demo.petpal.spring.back.entity.dto;

import fr.eql.akatz.demo.petpal.spring.back.entity.CatBreed;

import java.time.LocalDate;

public class CatDto {

    private String name;
    private LocalDate birthDate;
    private String picture;
    private CatBreed breed;
    private long ownerId;

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
