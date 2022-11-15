package fr.eql.akatz.demo.petpal.spring.back.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Entity
public class Cat extends Pet {

	@Enumerated(EnumType.STRING)
	private CatBreed breed;

	public Cat() {

	}

	public Cat(String name, CatBreed breed, LocalDate birthDate, String picture, Owner owner) {
		super(null, name, birthDate, picture, owner);
		this.breed = breed;
	}

	public CatBreed getBreed() {
		return breed;
	}
}
