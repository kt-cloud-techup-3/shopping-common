package com.kt.controller.admin;

import com.kt.constant.Gender;
import com.kt.constant.UserRole;
import com.kt.constant.UserStatus;
import com.kt.domain.entity.CourierEntity;
import com.kt.domain.entity.UserEntity;

import com.kt.repository.courier.CourierRepository;
import com.kt.repository.user.UserRepository;
import com.kt.security.DefaultCurrentUser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AdminAccountControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CourierRepository courierRepository;

	static final String TEST_PASSWORD = "1234561111";
	UserEntity testUser;
	CourierEntity testCourier;
	UserEntity testAdmin;

	@BeforeEach
	void setUp() {
		testAdmin = UserEntity.create(
			"테스트관리자1",
			"admintest@gmail.com",
			passwordEncoder.encode(TEST_PASSWORD),
			UserRole.ADMIN,
			Gender.MALE,
			LocalDate.of(1999, 1, 1),
			"010"
		);
		userRepository.save(testAdmin);

		testUser = UserEntity.create(
			"테스트유저1",
			"usertest@gmail.com",
			passwordEncoder.encode(TEST_PASSWORD),
			UserRole.MEMBER,
			Gender.MALE,
			LocalDate.of(2000, 1, 1),
			"010"
		);
		userRepository.save(testUser);

		testCourier = CourierEntity.create(
			"테스트기사1",
			"couriertest@gmail.com",
			passwordEncoder.encode(TEST_PASSWORD),
			Gender.MALE
		);
		courierRepository.save(testCourier);
	}

	@Test
	void 회원_목록_조회_성공() throws Exception {
		DefaultCurrentUser admin = new DefaultCurrentUser(
			testAdmin.getId(),
			testAdmin.getEmail(),
			UserRole.ADMIN
		);

		mockMvc.perform(get("/api/admin/accounts")
				.param("page", "1")
				.param("size", "10")
				.param("keyword", "")
				.with(SecurityMockMvcRequestPostProcessors.user(admin))
			)
			.andDo(print())
			.andExpectAll(
				status().isOk(),
				jsonPath("$.content").exists(),
				jsonPath("$.number").value(0)
			);
	}

	@Test
	void 회원_상세_조회_성공() throws Exception {
		DefaultCurrentUser admin = new DefaultCurrentUser(
			testAdmin.getId(),
			testAdmin.getEmail(),
			UserRole.ADMIN
		);

		mockMvc.perform(get("/api/admin/users/{userId}", testUser.getId())
				.with(SecurityMockMvcRequestPostProcessors.user(admin))
			)
			.andDo(print())
			.andExpectAll(
				status().isOk(),
				jsonPath("$.id").value(testUser.getId().toString()),
				jsonPath("$.email").value(testUser.getEmail())
			);
	}

	@Test
	void 회원_활성화_성공() throws Exception {
		// given
		testUser.disabled();
		userRepository.save(testUser);

		DefaultCurrentUser admin = new DefaultCurrentUser(
			testAdmin.getId(),
			testAdmin.getEmail(),
			UserRole.ADMIN
		);

		// when
		//mockMvc.perform(put("/api/admin/users/" + testUser.getId() + "/enabled")
			mockMvc.perform(put("/api/admin/users/{userId}/enabled", testUser.getId())
				.with(user(admin)))
				.andDo(print())
			.andExpect(status().isOk());

		// then
		UserEntity found = userRepository.findByIdOrThrow(testUser.getId());
		assertEquals(UserStatus.ENABLED, found.getStatus());
	}

	@Test
	void 회원_비활성화_성공() throws Exception {
		DefaultCurrentUser admin = new DefaultCurrentUser(
			testAdmin.getId(),
			testAdmin.getEmail(),
			UserRole.ADMIN
		);

		// when
		mockMvc.perform(put("/api/admin/users/{userId}/disabled", testUser.getId())
				.with(user(admin)))
			.andDo(print())
			.andExpect(status().isOk());

		// then
		UserEntity found = userRepository.findByIdOrThrow(testUser.getId());
		assertEquals(UserStatus.DISABLED, found.getStatus());
	}


	@Test
	void 회원_삭제_성공() throws Exception {
		DefaultCurrentUser admin = new DefaultCurrentUser(
			testAdmin.getId(),
			testAdmin.getEmail(),
			UserRole.ADMIN
		);

		mockMvc.perform(put("/api/admin/users/{userId}/removed", testUser.getId())
				.with(user(admin)))
			.andDo(print())
			.andExpect(status().isOk());

		UserEntity found = userRepository.findByIdOrThrow(testUser.getId());
		assertEquals(UserStatus.DELETED, found.getStatus());
	}


}
