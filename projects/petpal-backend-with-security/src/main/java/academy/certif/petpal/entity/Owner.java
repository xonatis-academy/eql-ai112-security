package academy.certif.petpal.entity;

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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class Owner implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String surname;
	@JsonIgnore
	@Column(unique = true, length = 32) // security : avoid conceptual duplicates to avoid unexpected behaviors
	private String login;
	@JsonIgnore
	private String password;
	@JsonIgnore
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private final List<Pet> pets = new ArrayList<>();
	@JsonIgnore
	@ElementCollection(targetClass=PetCategory.class, fetch = FetchType.LAZY)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name="owner_favorite_pet_category")
	@Column(name = "favorite_pet_category")
	private final List<PetCategory> favoritePetCategories = new ArrayList<>();

    
    @ManyToMany(fetch=FetchType.EAGER)
    private Collection<Role> roles;

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

    // Implementing methods for security

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return login;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

    // Adding setters for registration purposes
    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
