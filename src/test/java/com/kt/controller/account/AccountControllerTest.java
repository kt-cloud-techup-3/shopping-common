package com.kt.controller.account;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.constant.Gender;
import com.kt.constant.UserRole;
import com.kt.constant.UserStatus;
import com.kt.domain.dto.request.AccountRequest;
import com.kt.domain.entity.AbstractAccountEntity;
import com.kt.domain.entity.CourierEntity;
import com.kt.domain.entity.UserEntity;
import com.kt.repository.AccountRepository;
import com.kt.repository.courier.CourierRepository;
import com.kt.repository.user.UserRepository;
import com.kt.security.DefaultCurrentUser;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AccountControllerTest {
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CourierRepository courierRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;

	static final String TEST_PASSWORD = "1234567891011";
	UserEntity testUser;
	CourierEntity testCourier;

	@BeforeEach
	void setUp() throws Exception {
		testUser = UserEntity.create(
			"회원테스터",
			"wjd123@naver.com",
			passwordEncoder.encode(TEST_PASSWORD),
			UserRole.ADMIN,
			Gender.MALE,
			LocalDate.of(1990, 1, 1),
			"010-1234-5678"
		);
		userRepository.save(testUser);

		testCourier = CourierEntity.create(
			"배송기사테스터",
			"courier@naver.com",
			passwordEncoder.encode(TEST_PASSWORD),
			Gender.MALE
		);
		courierRepository.save(testCourier);
	}

	@Test
	void 비밀번호변경_테스트_성공() throws Exception {
		AccountRequest.UpdatePassword accountRequest = new AccountRequest.UpdatePassword(
			TEST_PASSWORD,
			"123456789101112"
		);
		String json = objectMapper.writeValueAsString(accountRequest);

		mockMvc.perform(patch("/api/accounts/{accountId}/password", testUser.getId())
			.with(SecurityMockMvcRequestPostProcessors.user("wjd123@naver.com"))
			.contentType(MediaType.APPLICATION_JSON)
			.content(json)
		).andExpect(status().isOk());

		AbstractAccountEntity foundedAccount = accountRepository.findByIdOrThrow(testUser.getId());

		Assertions.assertNotNull(foundedAccount);
		boolean result = passwordEncoder.matches("123456789101112", foundedAccount.getPassword());
		Assertions.assertTrue(result);
	}

	@Test
	void 배송기사탈퇴_테스트_성공() throws Exception {
		DefaultCurrentUser userDetails = new DefaultCurrentUser(
			testCourier.getId(),
			testCourier.getEmail(),
			testCourier.getRole()
		);

		mockMvc.perform(delete("/api/accounts/retire")
			.with(SecurityMockMvcRequestPostProcessors.user(userDetails))
		).andExpect(status().isOk());

		AbstractAccountEntity foundedAccount = accountRepository.findByIdOrThrow(testCourier.getId());
		Assertions.assertEquals(UserStatus.DELETED,foundedAccount.getStatus());
	}


}