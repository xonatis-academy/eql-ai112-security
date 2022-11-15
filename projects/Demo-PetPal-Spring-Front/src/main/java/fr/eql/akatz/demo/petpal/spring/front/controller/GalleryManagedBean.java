package fr.eql.akatz.demo.petpal.spring.front.controller;

import fr.eql.akatz.demo.petpal.spring.front.entity.Cat;
import fr.eql.akatz.demo.petpal.spring.front.entity.Owner;
import fr.eql.akatz.demo.petpal.spring.front.controller.webclient.GalleryWebClient;
import fr.eql.akatz.demo.petpal.spring.front.util.DateUtils;
import fr.eql.akatz.demo.petpal.spring.front.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.List;

@Component(value = "mbGallery")
@Scope(value = "session")
public class GalleryManagedBean implements Serializable {

	/** Injecté par le setter. */
	private GalleryWebClient galleryWebClient;

	private List<Owner> owners;
	private Owner selectedOwner;
	private Cat selectedCat;

	@PostConstruct
	public void init() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
		Owner connectedOwner = (Owner) session.getAttribute("connectedOwner");
		owners = findAllOwnersButSelf(connectedOwner);
	}

	public List<Owner> findAllOwnersButSelf(Owner self) {
		return galleryWebClient.findAllOwnersButSelf(self);
	}

	public Owner getOwnerUpdatedWithPets(Owner owner) {
		return owner != null ? galleryWebClient.getOwnerUpdatedWithPets(owner) : null;
	}

	public String caseCorrectedSelectedCatBreed() {
		return StringUtils.firstLetterCapitalized(selectedCat.getBreed().toString());
	}

	public String selectedCatFullBirthDate() {
		return DateUtils.fullDate(selectedCat.getBirthDate());
	}

	public void resetSelectedCat() {
		selectedCat = null;
	}

	/// Getters ///

	public List<Owner> getOwners() {
		return owners;
	}
	public Owner getSelectedOwner() {
		return selectedOwner;
	}
	public Cat getSelectedCat() {
		return selectedCat;
	}

	/// Setters ///

	@Autowired
	public void setGalleryWebClient(GalleryWebClient galleryWebClient) {
		this.galleryWebClient = galleryWebClient;
	}

	public void setSelectedOwner(Owner selectedOwner) {
		this.selectedOwner = selectedOwner;
	}
	public void setSelectedCat(Cat selectedCat) {
		this.selectedCat = selectedCat;
	}
}
