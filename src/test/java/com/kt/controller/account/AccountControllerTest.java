package com.kt.controller.account;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.common.api.ApiResult;
import com.kt.constant.Gender;
import com.kt.constant.UserRole;
import com.kt.constant.UserStatus;
import com.kt.domain.dto.request.AccountRequest;
import com.kt.domain.dto.response.AccountResponse;
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
		DefaultCurrentUser userDetails = new DefaultCurrentUser(
			testUser.getId(),
			testUser.getEmail(),
			testUser.getRole()
		);

		AccountRequest.UpdatePassword accountRequest = new AccountRequest.UpdatePassword(
			TEST_PASSWORD,
			"123456789101112"
		);
		String json = objectMapper.writeValueAsString(accountRequest);

		mockMvc.perform(patch("/api/accounts/password")
			.with(SecurityMockMvcRequestPostProcessors.user(userDetails))
			.contentType(MediaType.APPLICATION_JSON)
			.content(json)
		).andExpect(status().isOk());

		AbstractAccountEntity foundedAccount = accountRepository.findByIdOrThrow(testUser.getId());

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

	@Test
	void 내정보조회_성공() throws Exception {
		UserEntity testMember = UserEntity.create(
			"멤버테스터",
			"wjd123@naver.com",
			passwordEncoder.encode(TEST_PASSWORD),
			UserRole.MEMBER,
			Gender.MALE,
			LocalDate.of(1990, 1, 1),
			"010-1234-5678"
		);
		userRepository.save(testMember);
		DefaultCurrentUser userDetails = new DefaultCurrentUser(
			testMember.getId(),
			testMember.getEmail(),
			testMember.getRole()
		);

		MockHttpServletResponse response = mockMvc
			.perform(get("/api/accounts")
			.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn()
			.getResponse();

		String json = response.getContentAsString();

		ApiResult<AccountResponse.search> accountResponse = objectMapper.readValue(
			json,
			new TypeReference<ApiResult<AccountResponse.search>>() {}
		);
		AccountResponse.search responsedAccountSearch = accountResponse.getData();

		Assertions.assertEquals(responsedAccountSearch.accoundId(), testMember.getId());
		Assertions.assertEquals(responsedAccountSearch.email(), testMember.getEmail());
	}

	@Test
	void 내정보수정_성공() throws Exception {
		UserEntity testMember = UserEntity.create(
			"멤버테스터",
			"wjd123@naver.com",
			passwordEncoder.encode(TEST_PASSWORD),
			UserRole.MEMBER,
			Gender.MALE,
			LocalDate.of(1990, 1, 1),
			"010-1234-5678"
		);
		userRepository.save(testMember);

		AccountRequest.UpdateDetails updateDetails = new AccountRequest.UpdateDetails(
			"변경된테스터",
			"wjdtn@naver.com",
			Gender.FEMALE
		);
		String json = objectMapper.writeValueAsString(updateDetails);

		DefaultCurrentUser userDetails = new DefaultCurrentUser(
			testMember.getId(),
			testMember.getEmail(),
			testMember.getRole()
		);

		mockMvc.perform(put("/api/accounts")
				.with(SecurityMockMvcRequestPostProcessors.user(userDetails))
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk());

		Assertions.assertEquals(updateDetails.name(), testMember.getName());
		Assertions.assertEquals(updateDetails.email(), testMember.getEmail());
	}
}