package fr.eql.akatz.demo.petpal.spring.front.controller;

import fr.eql.akatz.demo.petpal.spring.front.entity.Cat;
import fr.eql.akatz.demo.petpal.spring.front.entity.CatBreed;
import fr.eql.akatz.demo.petpal.spring.front.entity.Owner;
import fr.eql.akatz.demo.petpal.spring.front.entity.Pet;
import fr.eql.akatz.demo.petpal.spring.front.controller.webclient.SpaceWebClient;
import fr.eql.akatz.demo.petpal.spring.front.entity.dto.CatDto;
import fr.eql.akatz.demo.petpal.spring.front.util.DateUtils;
import fr.eql.akatz.demo.petpal.spring.front.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component(value = "mbSpace")
@Scope(value = "request")
public class SpaceManagedBean implements Serializable {

	/** Injecté par le setter. */
	private SpaceWebClient spaceWebClient;

	private Owner connectedOwner;
	private Pet selectedPet;
	private List<String> catBreeds = new ArrayList<>();
	private String newCatName;
	private String newCatBreed;
	private Date newCatBirthDate;

	@PostConstruct
	public void init() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
		connectedOwner = (Owner) session.getAttribute("connectedOwner");
		connectedOwner = spaceWebClient.getOwnerUpdatedWithPets(connectedOwner);
		selectedPet = connectedOwner.getPets().get(0);
		catBreeds = Stream.of(CatBreed.values())
				.map(breed -> StringUtils.firstLetterCapitalized(breed.toString()))
				.collect(Collectors.toList());
	}

	public List<String> caseCorrectedFavoritePets() {
		return connectedOwner.getFavoritePetCategories().stream().map(
				pet -> StringUtils.firstLetterCapitalized(pet.toString())
		).collect(Collectors.toList());
	}

	public void updateSelectedPet(Pet pet) {
		selectedPet = pet;
	}

	public String fullDate(LocalDate date) {
		return DateUtils.fullDate(date);
	}

	public String catBreed(Pet pet) {
		Cat cat = (Cat) pet;
		return StringUtils.firstLetterCapitalized(cat.getBreed().toString());
	}

	public void saveCat() {
		CatDto catDto = new CatDto(
				newCatName,
				newCatBirthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
				"pictures/cat.jpg",
				CatBreed.valueOf(newCatBreed.toUpperCase()),
				connectedOwner.getId());
		spaceWebClient.saveCat(catDto);
		init();
	}

	/// Getters ///

	public Owner getConnectedOwner() {
		return connectedOwner;
	}
	public Pet getSelectedPet() {
		return selectedPet;
	}
	public List<String> getCatBreeds() {
		return catBreeds;
	}
	public String getNewCatName() {
		return newCatName;
	}
	public String getNewCatBreed() {
		return newCatBreed;
	}
	public Date getNewCatBirthDate() {
		return newCatBirthDate;
	}

	/// Setters ///

	@Autowired
	public void setSpaceWebClient(SpaceWebClient spaceWebClient) {
		this.spaceWebClient = spaceWebClient;
	}

	public void setConnectedOwner(Owner connectedOwner) {
		this.connectedOwner = connectedOwner;
	}
	public void setSelectedPet(Pet selectedPet) {
		this.selectedPet = selectedPet;
	}
	public void setNewCatName(String newCatName) {
		this.newCatName = newCatName;
	}
	public void setNewCatBreed(String newCatBreed) {
		this.newCatBreed = newCatBreed;
	}
	public void setNewCatBirthDate(Date newCatBirthDate) {
		this.newCatBirthDate = newCatBirthDate;
	}
}
