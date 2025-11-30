package com.kt.controller.review;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.constant.Gender;
import com.kt.constant.OrderProductStatus;
import com.kt.constant.UserRole;
import com.kt.domain.entity.CategoryEntity;
import com.kt.domain.entity.OrderEntity;
import com.kt.domain.entity.OrderProductEntity;
import com.kt.domain.entity.ProductEntity;
import com.kt.domain.entity.ReceiverVO;
import com.kt.domain.entity.ReviewEntity;
import com.kt.domain.entity.UserEntity;
import com.kt.repository.CategoryRepository;
import com.kt.repository.OrderRepository;
import com.kt.repository.orderproduct.OrderProductRepository;
import com.kt.repository.product.ProductRepository;
import com.kt.repository.review.ReviewRepository;
import com.kt.repository.user.UserRepository;
import com.kt.security.DefaultCurrentUser;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminReviewControllerTest {

	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	OrderProductRepository orderProductRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	MockMvc mockMvc;

	OrderProductEntity testOrderProduct;
	ProductEntity testProduct;
	DefaultCurrentUser admin;

	@BeforeEach
	void setUp() throws Exception {
		orderProductRepository.deleteAll();
		userRepository.deleteAll();
		productRepository.deleteAll();
		orderRepository.deleteAll();
		reviewRepository.deleteAll();
		categoryRepository.deleteAll();

		UserEntity user = UserEntity.create(
			"주문자테스터1",
			"wjd123@naver.com",
			"1234",
			UserRole.MEMBER,
			Gender.MALE,
			LocalDate.now(),
			"010-1234-5678"
		);
		userRepository.save(user);

		ReceiverVO receiver = new ReceiverVO(
			"수신자테스터1",
			"010-1234-5678",
			"강원도",
			"원주시",
			"행구로",
			"주소설명"
		);

		OrderEntity order = OrderEntity.create(
			receiver,
			user
		);
		orderRepository.save(order);

		CategoryEntity category = CategoryEntity.create("카테고리", null);
		categoryRepository.save(category);

		UserEntity testAdmin = UserEntity.create(
			"테스트관리자1",
			"admintest@gmail.com",
			"432432132131",
			UserRole.ADMIN,
			Gender.MALE,
			LocalDate.of(1999, 1, 1),
			"010"
		);
		admin = new DefaultCurrentUser(
			testAdmin.getId(),
			testAdmin.getEmail(),
			UserRole.ADMIN
		);

		testProduct = ProductEntity.create(
			"테스트상품명",
			1000L,
			5L,
			category
		);
		productRepository.save(testProduct);

		testOrderProduct = new OrderProductEntity(
			5L,
			5000L,
			OrderProductStatus.CREATED,
			order,
			testProduct
		);
		orderProductRepository.save(testOrderProduct);
	}


	@Test
	void 상품조회_성공() throws Exception {
		ReviewEntity review = ReviewEntity.create("테스트리뷰내용");
		review.mapToOrderProduct(testOrderProduct);
		reviewRepository.save(review);

		mockMvc.perform(
			get("/api/admin/reviews")
				.with(user(admin))
				.param("page", "1")
				.param("size", "10")
				.param("keyword", "테스트상품")
				.param("type", "NAME")
		).andExpect(status().isOk())
			.andExpect(jsonPath("$.data.content[0].reviewId").value(review.getId().toString()))
			.andExpect(jsonPath("$.data.content[0].content").value(review.getContent()));
	}
}