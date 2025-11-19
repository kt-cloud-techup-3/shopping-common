package com.kt.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kt.security.JwtFilter;
import com.kt.security.JwtProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

	private final JwtFilter jwtFilter;

	private static final String[] PERMIT_ALL = {
		"/api/auth/login",
		"/api/auth/signup/**"
	};

	private static final String[] MEMBER_URIS = {
		"/api/orders",
		"/api/returns/request",
		"/api/auth/password/reset",
		"/api/email/**",
		"/api/carts/**"
	};

	private static final String[] ADMIN_URIS = {
		"/api/categories/**"
	};

	private static final String[] COURIER_URIS = {
		"/api/shipping/**"
	};

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)

			// TODO: 인증 실패시 커스텀 예외 처리 핸들러 등록

			.authorizeHttpRequests(auth -> auth
				.requestMatchers(PERMIT_ALL).permitAll()
				.requestMatchers(MEMBER_URIS).hasRole("MEMBER")
				.requestMatchers(ADMIN_URIS).hasRole("ADMIN")
				.requestMatchers(COURIER_URIS).hasRole("COURIER")
				.anyRequest().authenticated()
			)
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}