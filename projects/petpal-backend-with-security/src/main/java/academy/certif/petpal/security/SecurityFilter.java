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