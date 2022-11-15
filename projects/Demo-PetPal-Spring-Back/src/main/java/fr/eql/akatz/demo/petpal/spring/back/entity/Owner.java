package fr.eql.akatz.demo.petpal.spring.back.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Owner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String surname;
	@JsonIgnore
	private String login;
	@JsonIgnore
	private String password;
	@JsonIgnore
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private final List<Pet> pets = new ArrayList<>();
	@ElementCollection(targetClass=PetCategory.class, fetch = FetchType.LAZY)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name="owner_favorite_pet_category")
	@Column(name = "favorite_pet_category")
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
}
