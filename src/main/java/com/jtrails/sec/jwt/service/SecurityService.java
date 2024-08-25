/**
 * 
 */
package com.jtrails.sec.jwt.service;

import java.security.Key;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * @author Lejil
 *
 */
@Component
public class SecurityService {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	@Value("${jwt.issuer}")
	private String issuer;

	private final Base64Service base64Service;

	public SecurityService(Base64Service base64Service) {
		this.base64Service = base64Service;
	}

	public String generateToken(UserDetails userDetails) {
		return Jwts.builder().setIssuer(issuer).setSubject(userDetails.getUsername())
				.claim("roles", getRoles(userDetails))
				.setIssuedAt(Date.from(Instant.ofEpochSecond(System.currentTimeMillis())))
				.setExpiration(Date.from(Instant.ofEpochSecond(System.currentTimeMillis() + expiration)))
				.signWith(hmacShaKey(secret)).compact();
	}

	public boolean isTokenValid(String token, String userName) {
		String username = getUserName(token);
		return (username.equals(userName) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		Date expirationDate = extractClaims(token).getExpiration();
		return expirationDate.before(new Date());
	}

	private Key hmacShaKey(String secret) {
		return Keys.hmacShaKeyFor(base64Service.getSigningKey(secret));
	}

	private String getRoles(UserDetails userDetails) {
		return userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
	}

	public Collection<GrantedAuthority> getGrantedAuthority(String token) {
		String extactedToken =extractClaims(token).get("roles").toString();
		return Arrays.stream(extactedToken.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

	}

	public String getUserName(String token) {
		return extractClaims(token).getSubject();
	}

	private Claims extractClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(hmacShaKey(secret)).build().parseClaimsJws(token).getBody();
	}

	public String decryptKey(String key) {
		return base64Service.decrypt(key);
	}

	public String encryptKey(String key) {
		return base64Service.encrypt(key);
	}

}
