package com.kt.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.kt.constant.Gender;
import com.kt.constant.OrderProductStatus;
import com.kt.constant.OrderStatus;
import com.kt.constant.UserRole;
import com.kt.domain.dto.response.AdminOrderResponse;
import com.kt.domain.entity.CategoryEntity;
import com.kt.domain.entity.OrderEntity;
import com.kt.domain.entity.OrderProductEntity;
import com.kt.domain.entity.ProductEntity;
import com.kt.domain.entity.ReceiverVO;
import com.kt.domain.entity.UserEntity;
import com.kt.repository.CategoryRepository;
import com.kt.repository.OrderProductRepository;
import com.kt.repository.OrderRepository;
import com.kt.repository.ProductRepository;
import com.kt.repository.ReviewRepository;
import com.kt.repository.user.UserRepository;

@SpringBootTest
@Transactional
class AdminOrderServiceTest {
	@Autowired
	private AdminOrderService adminOrderService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	private CategoryEntity defaultCategory;

	@BeforeEach
	void setup() {
		reviewRepository.deleteAll();
		orderProductRepository.deleteAll();
		orderRepository.deleteAll();
		productRepository.deleteAll();
		userRepository.deleteAll();
		categoryRepository.deleteAll();

		CategoryEntity category = CategoryEntity.create("테스트 기본 카테고리", null);
		defaultCategory = categoryRepository.save(category);
	}

	private UserEntity createUser(String email) {
		UserEntity user = UserEntity.create(
			"홍길동",
			email,
			"111",
			UserRole.ADMIN,
			Gender.MALE,
			LocalDate.now(),
			"0101010"
		);
		return userRepository.save(user);
	}

	private ProductEntity createProduct(String name, long stock) {
		ProductEntity product = ProductEntity.create(
			name,
			10L,
			stock,
			defaultCategory
		);
		return productRepository.save(product);
	}

	private OrderEntity createOrder(UserEntity user) {
		OrderEntity order = OrderEntity.create(
			ReceiverVO.create(
				"수령자",
				"0101234",
				"서울",
				"강남",
				"테헤란로",
				"100호"
			),
			user
		);
		return orderRepository.save(order);
	}

	private OrderProductEntity createOrderProduct(OrderEntity order, ProductEntity product, long quantity) {
		OrderProductEntity op = OrderProductEntity.create(
			quantity,
			product.getPrice(),
			OrderProductStatus.CREATED,
			order,
			product
		);
		return orderProductRepository.save(op);
	}


	@Test
	@DisplayName("주문_리스트_조회_성공")
	void 주문_리스트_조회_성공() {

		// given
		UserEntity user = createUser("adminOrder@example.com");
		OrderEntity order = createOrder(user);

		// when
		List<AdminOrderResponse.Search> result = adminOrderService.getOrders();

		// then
		assertThat(result).isNotEmpty();
		assertThat(result.get(0).orderId()).isEqualTo(order.getId());
	}

	@Test
	@DisplayName("주문_상세_조회_성공")
	void 주문_상세_조회_성공() {

		// given
		UserEntity user = createUser("admin@example.com");
		ProductEntity product = createProduct("상품1", 10000L);

		OrderEntity order = createOrder(user);
		createOrderProduct(order, product, 2L);

		// when
		AdminOrderResponse.Detail detail = adminOrderService.getOrder(order.getId());

		// then
		assertThat(detail.orderId()).isEqualTo(order.getId());
		assertThat(detail.products()).hasSize(1);
		assertThat(detail.products().get(0).quantity()).isEqualTo(2L);
	}

	@Test
	@DisplayName("주문_상태_변경_성공")
	void 주문_상태_변경_성공() {

		// given
		UserEntity user = createUser("admin2@example.com");
		OrderEntity order = createOrder(user);

		// when
		adminOrderService.changeStatus(order.getId(), OrderStatus.PAID);

		// then
		OrderEntity updated = orderRepository.findById(order.getId()).get();
		assertThat(updated.getStatus()).isEqualTo(OrderStatus.PAID);
	}


	@Test
	@DisplayName("주문_취소_성공__재고_복구됨")
	void 주문_취소_성공__재고_복구됨() {

		// given
		UserEntity user = createUser("admin3@example.com");
		ProductEntity product = createProduct("상품2", 10000L);

		OrderEntity order = createOrder(user);
		OrderProductEntity orderProduct = createOrderProduct(order, product, 2L);

		// when
		adminOrderService.cancelOrder(order.getId());

		// then
		ProductEntity updatedProduct = productRepository.findById(product.getId()).get();
		OrderEntity updatedOrder = orderRepository.findById(order.getId()).get();
		OrderProductEntity updatedOp = orderProductRepository.findById(orderProduct.getId()).get();

		assertThat(updatedProduct.getStock()).isEqualTo(10002L);
		assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.CANCELED);
		assertThat(updatedOp.getStatus()).isEqualTo(OrderProductStatus.CANCELED);
	}


}