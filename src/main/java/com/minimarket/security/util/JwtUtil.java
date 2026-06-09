package com.minimarket.security.util;

import com.minimarket.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {

	private final JwtProperties jwtProperties;
	private final Key key;

	public JwtUtil(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;

		byte[] keyBytes = jwtProperties.getSecret() != null
				? jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)
				: new byte[0];

		if (keyBytes.length < 32) {
			keyBytes = Arrays.copyOf(keyBytes, 32);
		}

		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(UserDetails userDetails) {
		long now = System.currentTimeMillis();
		List<String> roles = extractRoles(userDetails.getAuthorities());

		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.claim("roles", roles)
				.setIssuedAt(new Date(now))
				.setExpiration(new Date(now + jwtProperties.getExpiration()))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		return claimsResolver.apply(claims);
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private List<String> extractRoles(Collection<? extends GrantedAuthority> authorities) {
		return authorities.stream()
				.map(GrantedAuthority::getAuthority)
				.toList();
	}
}
