# 1. Ajouter les dépendances

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
```

Remarque : spring-boot-starter-security permet de bénéficier de Spring Security et jjwt permet de générer des JWT

# 2. Définir votre UserDetails

```java
/* File: /entity/Owner.java */

@Entity
public class Owner implements UserDetails {

    // ... ... ...

	@Column(unique = true, length = 32) // security : avoid conceptual duplicates to avoid unexpected behaviors
	private String login;

    private String password;
    
    // ... ... ...
    
    @ManyToMany(fetch=FetchType.EAGER)
    private Collection<Role> roles;

    // ... ... ...

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
```

Remarque : Ce qui marque un utilisateur, c'est cette ligne : `implements UserDetails`

# 3. Définir votre GrantedAuthority

```java
/* File: /entity/Role.java */

@Entity
public class Role implements GrantedAuthority {
    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String name;

    public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getAuthority() {
		return name;
	}
}
```

Remarque : Ce qui marque un role, c'est cette ligne : `implements GrantedAuthority`

# 4. Permettre la récupération d'un user via un Repository

```java
/* File: /repository/OwnerDao.java */

public interface OwnerDao extends JpaRepository<Owner, Long> {
    // ... ... ...

    // Adding for retrieving user from login - security
    Owner findByLogin(String login);
}
```

Remarque : Il faut pouvoir récupérer l'utilisateur avec son username ou son login

# 5. Créer une interface UserService et une interface JwtUserService

```java
/* File: /exception/AccountExistsException.java */

package academy.certif.petpal.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Account already exists")
public class AccountExistsException extends Exception {
    
}

```

Remarque : On prépare l'erreur à lancer si l'utilisateur qui veut s'inscrire existe deja

```java
/* File: /service/UserService.java */

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

```

Remarque : Permet de découpler les couches

```java
/* File: /service/JwtUserService.java */

package academy.certif.petpal.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtUserService extends UserService {

    String generateJwtForUser(UserDetails user);
    UserDetails getUserFromJwt(String jwt);

}
```

Remarque : Permet de découpler l'implémentation de la vérification utilisateur avec le JWT, peut-etre mocker dans les tests unitaires

# 6. Créer une implémentation du JwtUserService

```java
/* File: /service/impl/JwtUserServiceImpl.java */

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

```

Remarque : la classe concrète contient l'implémentation de l'interface

# 7. Créer un filter pour valider le user

```java
/* File: /security/SecurityFilter.java */

package academy.certif.petpal.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import academy.certif.petpal.service.JwtUserService;

public class SecurityFilter extends OncePerRequestFilter {
    
	@Autowired
	private JwtUserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
        try {
            String token = extractTokenFromHeader(request);
            UserDetails user = userService.getUserFromJwt(token);
            setPrincipalInSecurityContext(user);
        } catch (Exception e) {
            logger.info("Trying parse token but failed");
        }
        filterChain.doFilter(request, response);
	}

	private String extractTokenFromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

	private void setPrincipalInSecurityContext(UserDetails user) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(token);
	}
}
```

Remarque : vérifie l'utilisateur associé à la requete HTTP et transmet le principal (le user) au reste de l'application via le SecurityContextHolder

# 8. Créer un entry point pour les réponses par défaut

```java
/* File: /security/SecurityEntryPoint.java */

package academy.certif.petpal.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class SecurityEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }

}

```

Remarque : réponse par défaut des rêquetes sur des endpoints non publics

# 9. Configurer votre REST API avec le filtr et l'entry point

```java
/* File: /security/SecurityConfigurer.java */

package academy.certif.petpal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {


    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and();

        // Set unauthorized requests exception handler
        http = http
            .exceptionHandling()
            .authenticationEntryPoint(new SecurityEntryPoint())
            .and();

        // Set permissions on endpoints
        http
            .authorizeRequests()
            // Our public endpoints
            .antMatchers("/authorize").permitAll()
            .antMatchers("/register").permitAll()
            // Our private endpoints
            .anyRequest().authenticated();

        http
            .addFilterBefore(securityFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public SecurityFilter securityFilter() {
        return new SecurityFilter();
    }

}
```

Remarque : configuration de votre application avec le filter et le endpoint

# 10. Créer un endpoint /register pour l'inscription de nouveaux users

```java
/* File: /controller/SecurityController.java */

package academy.certif.petpal.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import academy.certif.petpal.entity.dto.AuthRequest;
import academy.certif.petpal.entity.dto.AuthResponse;
import academy.certif.petpal.exception.AccountExistsException;
import academy.certif.petpal.exception.UnauthorizedException;
import academy.certif.petpal.service.JwtUserService;

@RestController
public class SecurityController {

	@Autowired
	private JwtUserService userService;

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest requestDto) throws AccountExistsException {
        UserDetails user = userService.save(requestDto.getUsername(), requestDto.getPassword());
        String token = userService.generateJwtForUser(user);
		return ResponseEntity.ok(new AuthResponse(user, token));
	}
}

```

Remarque : ajouter un nouvel utilisateur et génère un JWT à la volée

# 11. Créer un endpoint /authorize pour générer un JWT pour un client

```java
/* File: /exception/UnauthorizedException.java */

package academy.certif.petpal.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends Exception {
    
}

```

Remarque : prépare une erreur à lancer si le JWT n'est pas valide

```java

/* File: /controller/SecurityController.java */

@RestController
public class SecurityController {

	@Autowired
	private JwtUserService userService;

    // ... ... ...

	@PostMapping("/authorize")
	public ResponseEntity<AuthResponse> authorize(@RequestBody AuthRequest requestDto) throws UnauthorizedException {
        Authentication authentication = null;
        try {
            authentication = userService.authenticate(requestDto.getUsername(), requestDto.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Token generation
            UserDetails user = (UserDetails) authentication.getPrincipal();
            String token = userService.generateJwtForUser(user);
            return ResponseEntity.ok(new AuthResponse(user, token));
        } catch(AuthenticationException e) {
            throw new UnauthorizedException();
        }
	}

}

```

Remarque : authentifie le principal (le user) à partir du JWT

# 12. Sécuriser vos endpoints avec @PreAuthorize

```java
/* File: /controller/rest/GlossaryRestController.java */

@RestController
@RequestMapping("glossary")
public class GlossaryRestController {

    /** Injecté par le setter. */
    GlossaryService glossaryService;

    /*
     * PreAuthorize supports SpEL (Spring Expression Language):
     * @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EDITOR')")
     * Without SpEL, it's quite the same as:
     * @RolesAllowed({ "ROLE_ADMIN", "ROLE_EDITOR" }) or just: @RolesAllowed("ROLE_ADMIN")
     * 
     * PS: @RolesAllowed requires JSR-250: jsr250Enabled = true in WebSecurityConfigurerAdapter
     * PS: @Secured without JSR-250 requires: securedEnabled  = true in WebSecurityConfigurerAdapter
     */

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/expressions")
    public List<String> findGlossaryExpressions() {
        return glossaryService.findGlossaryExpressions();
    }

    // ... ... ...
}
```

Remarque : on protège le endpoint `GET /glossary/expressions` avec le role `ROLE_ADMIN`. On peut faire cela sur tous nos endpoints.

# 13. Aller se dandiner sur un dancefloor

```java
/* File: haha */

/* boom chic chac boom */
/* boing boing yeah */
/* ... sous le soleil des tropiques ... */
```

Remarque : tu connais la chason ? =D
