package com.kt.api.user;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.common.MockMvcTest;
import com.kt.common.UserEntityCreator;
import com.kt.common.api.ApiResult;
import com.kt.domain.dto.response.UserResponse;
import com.kt.domain.entity.UserEntity;
import com.kt.repository.user.UserRepository;
import com.kt.security.DefaultCurrentUser;

@DisplayName("내 정보 조회(회원) - GET /api/users")
public class UserDetailTest extends MockMvcTest {
	@Autowired
	UserRepository userRepository;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;

	UserEntity testUser;
	DefaultCurrentUser userDetails;

	@BeforeEach
	void setUp() throws Exception {
		testUser = UserEntityCreator.createMember();
		userRepository.save(testUser);

		userDetails = new DefaultCurrentUser(
			testUser.getId(),
			testUser.getEmail(),
			testUser.getRole()
		);
	}

	@Test
	void 내정보조회_성공__200_OK() throws Exception {
		MockHttpServletResponse response = mockMvc.perform(get("/api/users")
				.with(user(userDetails)))
			.andExpect(status().isOk())
			.andReturn()
			.getResponse();

		String json = response.getContentAsString();

		ApiResult<UserResponse.UserDetail> accountResponse = objectMapper.readValue(
			json,
			new TypeReference<ApiResult<UserResponse.UserDetail>>() {}
		);
		UserResponse.UserDetail responseUserSearch = accountResponse.getData();

		Assertions.assertEquals(responseUserSearch.id(), testUser.getId());
		Assertions.assertEquals(responseUserSearch.email(), testUser.getEmail());
	}
}
