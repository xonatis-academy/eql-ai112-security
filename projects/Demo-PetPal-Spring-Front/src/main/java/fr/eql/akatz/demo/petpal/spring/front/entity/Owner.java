package fr.eql.akatz.demo.petpal.spring.front.entity;

import java.util.ArrayList;
import java.util.List;

public class Owner {

	private Long id;
	private String name;
	private String surname;
	private String login;
	private String password;
	private List<Pet> pets = new ArrayList<>();
	private final List<PetCategory> favoritePetCategories = new ArrayList<>();

	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getSurname() {
		return surname;
	}
	public List<Pet> getPets() {
		return pets;
	}
	public List<PetCategory> getFavoritePetCategories() {
		return favoritePetCategories;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}
}
