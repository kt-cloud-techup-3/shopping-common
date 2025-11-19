package com.kt.security;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.kt.domain.constant.UserRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {
	private final JwtProperties jwtProperties;
	private static final String ROLE_CLAIM_KEY = "role";
	private static final String LOGIN_ID_CLAIM_KEY = "loginId";

	public String issue(Long id, String loginId, UserRole role, Date expiration) {
		return Jwts.builder()
			.subject("kt-cloud-shopping")
			.issuer("GoFive")
			.issuedAt(new Date())
			.id(id.toString())
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
		} catch (UnsupportedJwtException e) {
			log.error("지원되지 않는 JWT 토큰: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("잘못된 형식의 JWT 토큰: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT 클레임 문자열이 비어있습니다: {}", e.getMessage());
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

		Long id = Long.valueOf(claims.getId());
		String loginId = claims.get(LOGIN_ID_CLAIM_KEY, String.class);
		UserRole role = UserRole.valueOf(claims.get(ROLE_CLAIM_KEY, String.class));

		return new DefaultCurrentUser(id, loginId, role);
	}

}
