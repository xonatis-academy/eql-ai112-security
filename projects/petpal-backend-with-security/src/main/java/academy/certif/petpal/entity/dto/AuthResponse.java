package academy.certif.petpal.entity.dto;

import org.springframework.security.core.userdetails.UserDetails;

public class AuthResponse {
	private String token;
	private UserDetails user;

    public AuthResponse(UserDetails user, String token) {
        this.user = user;
        this.token = token;
    }
	
	public String getToken() {
		return token;
	}
	public UserDetails getUser() {
		return user;
	}
	
}
