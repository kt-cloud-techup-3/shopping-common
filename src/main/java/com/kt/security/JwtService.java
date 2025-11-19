package com.kt.security;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.kt.domain.constant.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {
	private final JwtProperties jwtProperties;
	private static final String ROLE_CLAIM_KEY = "role";
	private static final String LOGIN_ID_CLAIM_KEY = "loginId";

	public String issue(UUID id, String loginId, UserRole role, Date expiration) {
		return Jwts.builder()
			.subject(id.toString())
			.issuer("GoFive")
			.issuedAt(new Date())
			.claim(ROLE_CLAIM_KEY, role.name())
			.claim(LOGIN_ID_CLAIM_KEY, loginId)
			.expiration(expiration)
			.signWith(jwtProperties.getSecret())
			.compact();
	}

	public Date getAccessExpiration() {
		return jwtProperties.getAccessTokenExpiration();
	}
	public Date getRefreshExpiration() {
		return jwtProperties.getRefreshTokenExpiration();
	}

	public boolean validate(String token) {
		try {
			parseClaims(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.warn("JWT 토큰 만료: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("유효하지 않은 JWT 토큰: {}", e.getMessage());
		}
		return false;
	}

	private Claims parseClaims(String token) {
		return Jwts.parser()
			.verifyWith(jwtProperties.getSecret())
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	public DefaultCurrentUser parseClaimsToCurrentUser(String token) {
		Claims claims = parseClaims(token);

		UUID id = UUID.fromString(claims.getSubject());
		String loginId = claims.get(LOGIN_ID_CLAIM_KEY, String.class);
		UserRole role = UserRole.valueOf(claims.get(ROLE_CLAIM_KEY, String.class));

		return new DefaultCurrentUser(id, loginId, role);
	}

}
