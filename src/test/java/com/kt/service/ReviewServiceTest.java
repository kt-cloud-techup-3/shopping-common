package com.kt.service;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.kt.constant.Gender;
import com.kt.constant.OrderProductStatus;
import com.kt.constant.ProductStatus;
import com.kt.constant.ReviewStatus;
import com.kt.constant.UserRole;
import com.kt.constant.message.ErrorCode;
import com.kt.domain.entity.OrderEntity;
import com.kt.domain.entity.OrderProductEntity;
import com.kt.domain.entity.ProductEntity;
import com.kt.domain.entity.ReceiverVO;
import com.kt.domain.entity.ReviewEntity;
import com.kt.domain.entity.UserEntity;
import com.kt.dto.response.ReviewResponse;
import com.kt.exception.BaseException;
import com.kt.repository.OrderProductRepository;
import com.kt.repository.OrderRepository;
import com.kt.repository.ProductRepository;
import com.kt.repository.ReviewRepository;
import com.kt.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ReviewServiceTest {

	@Autowired
	ReviewService reviewService;

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

	OrderProductEntity testOrderProduct;

	@BeforeEach
	void setUp() throws Exception {
		orderProductRepository.deleteAll();
		userRepository.deleteAll();
		productRepository.deleteAll();
		orderRepository.deleteAll();
		reviewRepository.deleteAll();

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

		ProductEntity product = ProductEntity.create(
			"테스트상품명",
			1000L,
			5L,
			ProductStatus.ACTIVATED
		);
		productRepository.save(product);

		testOrderProduct = new OrderProductEntity(
			5L,
			5000L,
			OrderProductStatus.CREATED,
			order,
			product
		);
		orderProductRepository.save(testOrderProduct);
	}


	@Test
	void 리뷰생성_성공() {
		reviewService.create(testOrderProduct.getId(), "테스트리뷰내용");

		ReviewEntity foundedReview = reviewRepository
			.findAll()
			.stream()
			.findFirst()
			.orElseThrow(() -> new BaseException(ErrorCode.REVIEW_NOT_FOUND));

		Assertions.assertEquals(testOrderProduct.getId(), foundedReview.getOrderProduct().getId());
	}

	@Test
	void 리뷰변경_성공() {
		ReviewEntity review = ReviewEntity.create("테스트리뷰내용");
		review.mapToOrderProduct(testOrderProduct);
		reviewRepository.save(review);

		reviewService.update(review.getId(), "변경된테스트리뷰내용");

		Assertions.assertEquals("변경된테스트리뷰내용", review.getContent());
	}

	@Test
	void 리뷰삭제_성공() {
		ReviewEntity review = ReviewEntity.create("테스트리뷰내용");
		review.mapToOrderProduct(testOrderProduct);
		reviewRepository.save(review);
		reviewService.delete(review.getId());
		Assertions.assertEquals(ReviewStatus.REMOVED, review.getStatus());
	}

	@Test
	void 리뷰조회_성공(){
		ReviewEntity review = ReviewEntity.create("테스트리뷰내용");
		review.mapToOrderProduct(testOrderProduct);
		reviewRepository.save(review);

		ReviewResponse.Search foundedReviewDto = reviewService.getReview(testOrderProduct.getId());

		Assertions.assertEquals(review.getId(),foundedReviewDto.reviewId());
	}
}