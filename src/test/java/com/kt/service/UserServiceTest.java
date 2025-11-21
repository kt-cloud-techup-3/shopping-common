package com.kt.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.kt.constant.Gender;
import com.kt.constant.OrderProductStatus;
import com.kt.constant.ProductStatus;
import com.kt.constant.UserRole;
import com.kt.domain.entity.OrderEntity;
import com.kt.domain.entity.OrderProductEntity;
import com.kt.domain.entity.ProductEntity;
import com.kt.domain.entity.ReceiverVO;
import com.kt.domain.entity.ReviewEntity;
import com.kt.domain.entity.UserEntity;
import com.kt.dto.response.OrderProductResponse;
import com.kt.repository.OrderProductRepository;
import com.kt.repository.OrderRepository;
import com.kt.repository.ProductRepository;
import com.kt.repository.ReviewRepository;
import com.kt.repository.UserRepository;
import java.util.List;
import com.kt.constant.Gender;
import com.kt.constant.ProductStatus;
import com.kt.constant.UserRole;
import com.kt.domain.dto.response.UserResponse;
import com.kt.domain.entity.OrderEntity;
import com.kt.domain.entity.ProductEntity;
import com.kt.domain.entity.ReceiverVO;
import com.kt.domain.entity.UserEntity;
import com.kt.repository.OrderRepository;
import com.kt.repository.ProductRepository;
import com.kt.repository.UserRepository;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserServiceTest {


	private UUID userId;
	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	OrderProductRepository orderProductRepository;
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	ProductRepository productRepository;

	UserEntity testUser;
	OrderEntity testOrder;
	ProductEntity testProduct;

	@BeforeEach
	void setUp() throws Exception {
		userRepository.deleteAll();
		orderRepository.deleteAll();
		orderProductRepository.deleteAll();
		reviewRepository.deleteAll();
		productRepository.deleteAll();

		testUser = UserEntity.create(
			"주문자테스터1",
			"wjd123@naver.com",
			"1234",
			UserRole.MEMBER,
			Gender.MALE,
			LocalDate.of(1990, 1, 1),
			"010-1234-5678"
		);
		userRepository.save(testUser);

		ReceiverVO receiver = new ReceiverVO(
			"수신자테스터1",
			"010-1234-5678",
			"강원도",
			"원주시",
			"행구로",
			"주소설명"
		);

		testOrder = OrderEntity.create(
			receiver,
			testUser
		);
		orderRepository.save(testOrder);

		testProduct= ProductEntity.create(
			"테스트상품명",
			1000L,
			5L,
			ProductStatus.ACTIVATED
		);
		productRepository.save(testProduct);
	}

	@Test
	void 내_주문_조회() {
		UserEntity savedUser = userRepository.save(
			UserEntity.create(
				"김도현",
				"ddd",
				"111",
				UserRole.MEMBER,
				Gender.MALE,
				LocalDate.now(),
				"0101010"
			)
		);

		userId = savedUser.getId();

		productRepository.save(
			ProductEntity.create(
				"테스트물건",
				3L,
				3L,
				ProductStatus.ACTIVATED
			)
		);

		orderRepository.save(
			OrderEntity.create(
				ReceiverVO.create("이름", "번호", "도시", "시군구", "동", "상세"),
				savedUser
			)
		);
		// when
		UserResponse.Orders foundOrder = userService.getOrdersByUserId(userId);

		// then
		assertThat(foundOrder).isNotNull();
		assertThat(foundOrder.userId()).isEqualTo(userId);
		assertThat(foundOrder.orders()).isNotEmpty();
	}


	@Test
	void 리뷰_가능한_주문상품_존재(){
		OrderProductEntity orderProduct = new OrderProductEntity(
			5L,
			5000L,
			OrderProductStatus.PURCHASE_CONFIRMED,
			testOrder,
			testProduct
		);
		orderProductRepository.save(orderProduct);

		OrderProductResponse.SearchReviewable foundedOrderProductResponse = userService
			.getReviewableOrderProducts(testUser.getId())
			.stream()
			.findFirst()
			.orElse(null);

		Assertions.assertNotNull(foundedOrderProductResponse);
		Assertions.assertEquals(orderProduct.getId(), foundedOrderProductResponse.orderProductId());
	}

	@Test
	void 리뷰_가능한_주문상품_없음__작성한_리뷰_존재(){
		OrderProductEntity orderProduct = new OrderProductEntity(
			5L,
			5000L,
			OrderProductStatus.PURCHASE_CONFIRMED,
			testOrder,
			testProduct
		);
		orderProductRepository.save(orderProduct);
		ReviewEntity review = ReviewEntity.create(
			"테스트리뷰내용"
		);
		review.mapToOrderProduct(orderProduct);
		reviewRepository.save(review);


		List<OrderProductResponse.SearchReviewable> foundedOrderProductResponses = userService
			.getReviewableOrderProducts(testUser.getId());

		Assertions.assertEquals(0,foundedOrderProductResponses.size());
	}


	@Test
	void 리뷰_가능한_주문상품_없음__주문상품_상태_구매확정_아님(){
		OrderProductEntity orderProduct = new OrderProductEntity(
			5L,
			5000L,
			OrderProductStatus.CREATED,
			testOrder,
			testProduct
		);
		orderProductRepository.save(orderProduct);

		List<OrderProductResponse.SearchReviewable> foundedOrderProductResponses = userService
			.getReviewableOrderProducts(testUser.getId());

		Assertions.assertEquals(0,foundedOrderProductResponses.size());
	}
}