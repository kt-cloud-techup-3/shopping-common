package com.kt.api.user;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.common.TestWithMockMvc;
import com.kt.common.UserEntityCreator;
import com.kt.constant.Gender;
import com.kt.domain.dto.request.UserRequest;
import com.kt.domain.entity.UserEntity;
import com.kt.repository.user.UserRepository;
import com.kt.security.DefaultCurrentUser;

@DisplayName("내 정보 수정(회원) - PUT /api/users")
public class UserDetailUpdateTest extends TestWithMockMvc {
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
	void 내정보수정_성공__200_OK() throws Exception {
		UserRequest.UpdateDetails updateDetails = new UserRequest.UpdateDetails(
			"변경된테스터",
			"010-5678-1234",
			LocalDate.of(1880, 3, 4),
			Gender.FEMALE
		);
		String json = objectMapper.writeValueAsString(updateDetails);

		mockMvc.perform(put("/api/users")
				.with(user(userDetails))
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andExpect(status().isOk());

		Assertions.assertEquals(updateDetails.name(), testUser.getName());
		Assertions.assertEquals(updateDetails.birth(), testUser.getBirth());
	}
}
