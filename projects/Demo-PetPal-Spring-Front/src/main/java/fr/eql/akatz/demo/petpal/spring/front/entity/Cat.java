package fr.eql.akatz.demo.petpal.spring.front.entity;

import java.time.LocalDate;

public class Cat extends Pet {

	private CatBreed breed;

	public Cat() {

	}

	public Cat(String name, CatBreed breed, LocalDate birthDate, String picture, Owner owner) {
		super(name, birthDate, picture, owner);
		this.breed = breed;
	}

	public CatBreed getBreed() {
		return breed;
	}
}
