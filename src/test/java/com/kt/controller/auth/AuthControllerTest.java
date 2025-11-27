package com.kt.controller.auth;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.constant.redis.RedisKey;
import com.kt.domain.dto.request.SignupRequest;
import com.kt.infra.mail.EmailClient;
import com.kt.infra.redis.RedisCache;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AuthControllerTest {
	@Autowired
	ObjectMapper objectMapper;
	String TEST_EMAIL = "kimdohyun032@gmail.com";
	@Autowired
	private RedisCache redisCache;
	@Autowired
	private EmailClient emailClient;
	@Autowired
	private MockMvc mockMvc;

	@Test
	void 인증코드_발송_성공() throws Exception {

		// given
		var request = new SignupRequest.SignupEmail(TEST_EMAIL);

		// when
		mockMvc.perform(
				post("/api/auth/email/code")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request))
			)
			.andDo(print())
			.andExpectAll(
				status().isOk(),
				jsonPath("$.code").value("ok"),
				jsonPath("$.message").value("성공")
			);

		// then
		assertThat(redisCache.hasKey(RedisKey.SIGNUP_CODE.key(TEST_EMAIL))).isEqualTo(true);
		String redisAuthCode = redisCache.get(
			RedisKey.SIGNUP_CODE.key(TEST_EMAIL),
			String.class
		);
		log.info("이메일 인증코드: - 발송: {}", redisAuthCode);
		assertThat(redisAuthCode).isNotNull();
	}

	@Test
	void 인증코드_인증_성공() throws Exception {

		// given
		String redisAuthCode = "123456";
		redisCache.set(RedisKey.SIGNUP_CODE, TEST_EMAIL, redisAuthCode);

		var request = new SignupRequest.VerifySignupCode(TEST_EMAIL, redisAuthCode);

		// then
		assertThat(redisAuthCode).isNotNull();
		log.info("이메일 인증코드 - 인증: {}", redisAuthCode);

		mockMvc.perform(post("/api/auth/email/verify")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andDo(print())
			.andExpectAll(
				status().isOk(),
				jsonPath("$.code").value("ok"),
				jsonPath("$.message").value("성공")
			);

	}
}