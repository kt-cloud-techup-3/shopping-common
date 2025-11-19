package com.kt.security;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	private static final String TOKEN_PREFIX = "Bearer ";

	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String header = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = header.substring(TOKEN_PREFIX.length());

		if (!jwtService.validate(token)) {
			log.info("유효하지 않거나 만료된 JWT 토큰 감지: {}", request.getRequestURI());
			filterChain.doFilter(request, response);
			return;
		}

		try {
			DefaultCurrentUser currentUser = jwtService.parseClaimsToCurrentUser(token);
			AuthenticationToken authenticationToken = new AuthenticationToken(
				currentUser,
				currentUser.getAuthorities()
			);
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		} catch (Exception e) {
			log.error("JWT 파싱 중 오류 발생", e);
		}
		filterChain.doFilter(request, response);
	}
}