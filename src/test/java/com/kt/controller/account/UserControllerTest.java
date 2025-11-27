package com.kt.controller.account;

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
import com.kt.domain.dto.request.UserRequest;
import com.kt.domain.dto.response.UserResponse;
import com.kt.domain.entity.UserEntity;
import com.kt.repository.user.UserRepository;
import com.kt.security.DefaultCurrentUser;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerTest {

	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;

	static final String TEST_PASSWORD = "1234567891011";
	UserEntity testMember;

	@BeforeEach
	void setUp() throws Exception {
		testMember = UserEntity.create(
			"멤버테스터",
			"member@naver.com",
			passwordEncoder.encode(TEST_PASSWORD),
			UserRole.MEMBER,
			Gender.MALE,
			LocalDate.of(1990, 1, 1),
			"010-1234-5678"
		);
		userRepository.save(testMember);
	}

	@Test
	void 내정보조회_성공() throws Exception {
		DefaultCurrentUser userDetails = new DefaultCurrentUser(
			testMember.getId(),
			testMember.getEmail(),
			testMember.getRole()
		);

		MockHttpServletResponse response = mockMvc
			.perform(get("/api/users")
				.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
			.andExpect(status().isOk())
			.andReturn()
			.getResponse();

		String json = response.getContentAsString();

		ApiResult<UserResponse.Detail> accountResponse = objectMapper.readValue(
			json,
			new TypeReference<ApiResult<UserResponse.Detail>>() {}
		);
		UserResponse.Detail responseUserSearch = accountResponse.getData();

		Assertions.assertEquals(responseUserSearch.id(), testMember.getId());
		Assertions.assertEquals(responseUserSearch.email(), testMember.getEmail());
	}


	@Test
	void 내정보수정_성공() throws Exception {
		UserRequest.UpdateDetails updateDetails = new UserRequest.UpdateDetails(
			"변경된테스터",
			"010-5678-1234",
			LocalDate.of(1880, 3, 4),
			Gender.FEMALE
		);
		String json = objectMapper.writeValueAsString(updateDetails);

		DefaultCurrentUser userDetails = new DefaultCurrentUser(
			testMember.getId(),
			testMember.getEmail(),
			testMember.getRole()
		);

		mockMvc.perform(put("/api/users")
				.with(SecurityMockMvcRequestPostProcessors.user(userDetails))
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk());

		Assertions.assertEquals(updateDetails.name(), testMember.getName());
		Assertions.assertEquals(updateDetails.birth(), testMember.getBirth());
	}
}