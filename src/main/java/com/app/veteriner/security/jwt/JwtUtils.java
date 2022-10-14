package com.app.veteriner.security.jwt;


import com.app.veteriner.security.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;


import java.util.Date;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${veteriner.app.jwtSecret}")
	private String jwtSecret;

	@Value("${veteriner.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	public static SimpleGrantedAuthority convertAuthority(String role)
	{
		String formattedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;

		return new SimpleGrantedAuthority(formattedRole);
	}
	public String generateJwtToken(Authentication authentication) {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		Date expireDate = new Date(new Date().getTime() + jwtExpirationMs);
		return Jwts.builder().setSubject(userDetails.getUsername())
				.setIssuedAt(new Date()).setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return !isTokenExpired(authToken);
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	private boolean isTokenExpired(String authToken) {
		Date expiration = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody().getExpiration();
		return expiration.before(new Date());
	}

	public String generateTokenFromUsername(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

}
