package com.klu.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.klu.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	@Value("${app.jwt.secret}")
	private String jwtSecret;

	@Value("${app.jwt.expiration-ms}")
	private long jwtExpirationMs;

	public String generateToken(User user) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

		return Jwts.builder()
				.subject(user.getEmail())
				.claim("userId", user.getId())
				.claim("role", user.getRole())
				.claim("name", user.getName())
				.issuedAt(now)
				.expiration(expiryDate)
				.signWith(signingKey(), Jwts.SIG.HS256)
				.compact();
	}

	public String extractUsername(String token) {
		return Jwts.parser()
				.verifyWith(signingKey())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}

	public boolean isTokenValid(String token, String email) {
		String subject = extractUsername(token);
		return subject != null && subject.equalsIgnoreCase(email) && !isTokenExpired(token);
	}

	public long getExpirationMs() {
		return jwtExpirationMs;
	}

	private boolean isTokenExpired(String token) {
		Date expiration = Jwts.parser()
				.verifyWith(signingKey())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getExpiration();
		return expiration.before(new Date());
	}

	private SecretKey signingKey() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}
}