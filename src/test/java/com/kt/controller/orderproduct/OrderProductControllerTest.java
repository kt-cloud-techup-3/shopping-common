package com.kt.controller.orderproduct;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.constant.Gender;
import com.kt.constant.OrderProductStatus;
import com.kt.constant.UserRole;
import com.kt.domain.dto.request.ReviewRequest;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrderProductControllerTest {

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
	@Autowired
	ObjectMapper objectMapper;

	OrderProductEntity testOrderProduct;
	ProductEntity testProduct;

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
	void 상품리뷰작성_성공() throws Exception {
		ReviewRequest.Create reviewCreate = new ReviewRequest.Create(
			"생성한테스트리뷰내용"
		);
		String json = objectMapper.writeValueAsString(reviewCreate);

		mockMvc.perform(post("/api/orderproducts/{orderProductId}/reviews", testOrderProduct.getId())
			.with(user("wjd123@naver.com"))
			.contentType(MediaType.APPLICATION_JSON)
			.content(json)
		).andExpect(status().isOk());

		ReviewEntity savedReview = reviewRepository.findByOrderProductIdOrThrow(testOrderProduct.getId());

		Assertions.assertNotNull(savedReview);
		Assertions.assertEquals(savedReview.getContent(),reviewCreate.content());
	}
}