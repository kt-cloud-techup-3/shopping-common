package com.kt.api.auth;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.kt.constant.Gender;
import com.kt.constant.UserRole;
import com.kt.controller.auth.LoginResponse;
import com.kt.domain.dto.request.LoginRequest;
import com.kt.domain.entity.UserEntity;
import com.kt.repository.user.UserRepository;

@DisplayName("POST /api/auth")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class POST_spec {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		userRepository.deleteAll();

		UserEntity member = UserEntity.create(
			"test",
			"test@test.com",
			passwordEncoder.encode("1234"),
			UserRole.MEMBER,
			Gender.MALE,
			LocalDate.now(),
			"010-1234-5678"
		);
		userRepository.save(member);
	}

	@Test
	void 로그인_성공(
		@Autowired TestRestTemplate client
	) {
		LoginRequest request = new LoginRequest("test@test.com", "1234");
		ResponseEntity<LoginResponse> response = client.postForEntity("/api/auth/login", request, LoginResponse.class);
		LoginResponse body = response.getBody();

		System.out.println(response.getStatusCodeValue());
		System.out.println(body.accessToken());
		System.out.println(body.refreshToken());

		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(body.accessToken()).isNotBlank();
		assertThat(body.refreshToken()).isNotBlank();
	}
}
