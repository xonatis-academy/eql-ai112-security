package academy.certif.petpal.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtUserService extends UserService {

    String generateJwtForUser(UserDetails user);
    UserDetails getUserFromJwt(String jwt);

}
