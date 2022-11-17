package academy.certif.petpal.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import academy.certif.petpal.entity.Owner;
import academy.certif.petpal.exception.AccountExistsException;
import academy.certif.petpal.repository.OwnerDao;
import academy.certif.petpal.service.JwtUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUserServiceImpl implements JwtUserService {

    @Autowired
    private OwnerDao ownerDao;

    @Autowired
	AuthenticationManager authenticationManager;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    private final String signingKey;

    public JwtUserServiceImpl(@Value("${jwt.signing.key}") String signingKey) {
        this.signingKey = signingKey;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Owner owner = ownerDao.findByLogin(username);
        if (owner == null) {
            throw new UsernameNotFoundException("The owner could not be found");
        }
        return owner;
    }

    // Used for registration
    public UserDetails save(String username, String password) throws AccountExistsException {
        if (ownerDao.findByLogin(username) != null) {
            throw new AccountExistsException();
        }
        Owner owner = new Owner();
		owner.setLogin(username);
		owner.setPassword(passwordEncoder().encode(password));
		ownerDao.save(owner);
        return owner;
    }

    // Used for authentication
    public UserDetails getUserFromJwt(String jwt) {
        String username = getUsernameFromToken(jwt);
        return loadUserByUsername(username);
    }

    private String getUsernameFromToken(String token) {
        System.out.println(signingKey);
        Claims claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String generateJwtForUser(UserDetails user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600 * 1000);
        return Jwts.builder().setSubject(user.getUsername()).setIssuedAt(now).setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, signingKey)
            .compact(); 
    }

    public Authentication authenticate(String username, String password) throws AuthenticationException {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authentication);
    }
    
}
