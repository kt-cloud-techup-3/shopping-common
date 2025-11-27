package com.kt.controller.product;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.constant.Gender;
import com.kt.constant.UserRole;
import com.kt.domain.dto.request.AdminProductRequest;
import com.kt.domain.dto.request.LoginRequest;
import com.kt.domain.entity.CategoryEntity;
import com.kt.domain.entity.ProductEntity;
import com.kt.domain.entity.UserEntity;
import com.kt.repository.CategoryRepository;
import com.kt.repository.product.ProductRepository;
import com.kt.repository.user.UserRepository;
import com.kt.security.DefaultCurrentUser;

@AutoConfigureMockMvc
@SpringBootTest
	// @Transactional
class AdminProductControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void 로그인_성공() throws Exception {
		//given

		// when
		LoginRequest request = new LoginRequest("test@test.com", "1234");
		ResultActions actions = mockMvc.perform(
			post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON.toString())
				.content(objectMapper.writeValueAsString(request))
		);

		// then
		actions.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void 상품_생성_성공() throws Exception {
		// given
		CategoryEntity category = CategoryEntity.create("카테고리", null);
		categoryRepository.save(category);
		// ProductEntity product = ProductEntity.create("상품명", 1000L, 100000L, category);
		// productRepository.save(product);
		// when
		AdminProductRequest.Create request = new AdminProductRequest.Create(
			"상품명",
			1000L,
			100000L,
			category.getId()
		);
		ResultActions actions = mockMvc.perform(
			post("/api/admin/products")
				.contentType(MediaType.APPLICATION_JSON.toString())
				.content(objectMapper.writeValueAsString(request))
		);

		// then
		actions.andExpect(status().isOk())
			.andDo(print());

		Optional<ProductEntity> found = productRepository.findAll()
			.stream()
			.filter(product -> product.getName().equals("상품명"))
			.findFirst();

		assertThat(found.isPresent()).isTrue();
	}

	@Test
	void 상품_생성_성공_2() throws Exception {
		String TEST_PASSWORD = "testpassword";
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

		CategoryEntity category = CategoryEntity.create("카테고리", null);
		categoryRepository.save(category);

		AdminProductRequest.Create request = new AdminProductRequest.Create(
			"상품명",
			1000L,
			100000L,
			category.getId()
		);

		mockMvc
			.perform(post("/api/admin/products")
				.contentType(MediaType.APPLICATION_JSON.toString())
				.content(objectMapper.writeValueAsString(request))
				.with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
			.andExpect(status().isOk())
			.andDo(print());
	}

}