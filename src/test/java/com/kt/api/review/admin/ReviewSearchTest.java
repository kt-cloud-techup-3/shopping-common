package com.kt.api.review.admin;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.common.CurrentUserCreator;
import com.kt.common.OrderProductCreator;
import com.kt.common.ProductCreator;
import com.kt.common.ReceiverCreator;
import com.kt.common.TestWithMockMvc;
import com.kt.common.UserEntityCreator;
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

@DisplayName("상품 리뷰 조회 (어드민) - GET /api/admin/reviews")
public class ReviewSearchTest extends TestWithMockMvc {

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

	@BeforeEach
	void setUp() throws Exception {
		UserEntity user = UserEntityCreator.createMember();
		userRepository.save(user);

		ReceiverVO receiver = ReceiverCreator.createReceiver();

		OrderEntity order = OrderEntity.create(receiver, user);
		orderRepository.save(order);

		CategoryEntity category = CategoryEntity.create("카테고리", null);
		categoryRepository.save(category);

		testProduct = ProductCreator.createProduct(category);
		productRepository.save(testProduct);

		testOrderProduct = OrderProductCreator.createOrderProduct(order, testProduct);
		orderProductRepository.save(testOrderProduct);
	}

	@Test
	void 상품리뷰조회_성공__200_OK() throws Exception {
		ReviewEntity review = ReviewEntity.create("테스트리뷰내용");
		review.mapToOrderProduct(testOrderProduct);
		reviewRepository.save(review);

		mockMvc.perform(
				get("/api/admin/reviews")
					.with(user(CurrentUserCreator.getAdminUserDetails()))
					.param("page", "1")
					.param("size", "10")
					.param("keyword", "테스트상품")
					.param("type", "NAME")
			).andExpect(status().isOk())
			.andExpect(jsonPath("$.data.content[0].reviewId").value(review.getId().toString()))
			.andExpect(jsonPath("$.data.content[0].content").value(review.getContent()));
	}
}
