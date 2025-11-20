package com.kt.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.kt.constant.Gender;
import com.kt.constant.OrderProductStatus;
import com.kt.constant.ProductStatus;
import com.kt.constant.UserRole;
import com.kt.constant.message.ErrorCode;
import com.kt.domain.entity.OrderEntity;
import com.kt.domain.entity.OrderProductEntity;
import com.kt.domain.entity.ProductEntity;
import com.kt.domain.entity.ReceiverVO;
import com.kt.domain.entity.ReviewEntity;
import com.kt.domain.entity.UserEntity;
import com.kt.exception.BaseException;
import com.kt.repository.OrderProductRepository;
import com.kt.repository.OrderRepository;
import com.kt.repository.ProductRepository;
import com.kt.repository.ReviewRepository;
import com.kt.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserServiceTest {

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
	void 리뷰_가능_주문상품_검색_성공(){
		OrderProductEntity orderProduct = new OrderProductEntity(
			5L,
			5000L,
			OrderProductStatus.PURCHASE_CONFIRMED,
			testOrder,
			testProduct
		);
		orderProductRepository.save(orderProduct);

		OrderProductEntity foundedOrderProduct = userService
			.getReviewableOrderProducts(testUser.getId())
			.stream()
			.findFirst()
			.orElseThrow(()-> new BaseException(ErrorCode.REVIEW_NOT_FOUND));

		Assertions.assertEquals(orderProduct.getId(), foundedOrderProduct.getId());
	}

	@Test
	void 리뷰_가능_주문상품_검색_실패__작성한_리뷰_존재(){
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

		assertThrowsExactly(
			BaseException.class,
			()->{
				userService
					.getReviewableOrderProducts(testUser.getId())
					.stream()
					.findFirst()
					.orElseThrow(()-> new BaseException(ErrorCode.REVIEW_NOT_FOUND));
			}
		);
	}


	@Test
	void 리뷰_가능_주문상품_검색_실패__주문상품_상태_구매확정_아님(){
		OrderProductEntity orderProduct = new OrderProductEntity(
			5L,
			5000L,
			OrderProductStatus.CREATED,
			testOrder,
			testProduct
		);
		orderProductRepository.save(orderProduct);

		assertThrowsExactly(
			BaseException.class,
			()->{
				userService
					.getReviewableOrderProducts(testUser.getId())
					.stream()
					.findFirst()
					.orElseThrow(()-> new BaseException(ErrorCode.REVIEW_NOT_FOUND));
			}
		);
	}
}