package academy.certif.petpal.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import academy.certif.petpal.exception.AccountExistsException;

public interface UserService extends UserDetailsService {
    
    Authentication authenticate(String username, String password) throws AuthenticationException;
    UserDetails save(String username, String password) throws AccountExistsException;

}
