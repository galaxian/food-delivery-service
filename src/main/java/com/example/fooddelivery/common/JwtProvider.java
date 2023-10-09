package com.example.fooddelivery.common;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

	private static final String TYPE_CLAIM_KEY = "type";

	private final String secretKey;
	private final Long tokenValidity;

	public JwtProvider(@Value("${jwt.secret}") String secretKey,
		@Value("${jwt.token-validity}") Long tokenValidity) {
		this.secretKey = secretKey;
		this.tokenValidity = tokenValidity;
	}

	public String createAccessToken(String subject, Map<String, Object> claims) {
		Map<String, Object> copiedClaims = new HashMap<>(claims);
		copiedClaims.put(TYPE_CLAIM_KEY, "Access");
		return createToken(tokenValidity, subject, copiedClaims);
	}

	private String createToken(Long tokenValidity, String subject, Map<String, Object> claims) {
		SecretKey signKey = createKey();
		Date expiration = createExpiration(tokenValidity);
		return Jwts.builder()
			.signWith(signKey)
			.setClaims(claims)
			.setSubject(subject)
			.setExpiration(expiration)
			.compact();
	}

	private Date createExpiration(Long validity) {
		long now = new Date().getTime();
		return new Date(now + validity);
	}

	private SecretKey createKey() {
		byte[] secretKeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(secretKeyBytes);
	}
}
