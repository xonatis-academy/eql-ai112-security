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

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest requestDto) throws AccountExistsException {
        UserDetails user = userService.save(requestDto.getUsername(), requestDto.getPassword());
        String token = userService.generateJwtForUser(user);
		return ResponseEntity.ok(new AuthResponse(user, token));
	}
}
