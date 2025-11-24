package com.kt.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.kt.constant.Gender;
import com.kt.constant.OrderProductStatus;
import com.kt.constant.OrderStatus;
import com.kt.constant.ProductStatus;
import com.kt.constant.UserRole;
import com.kt.domain.dto.request.OrderRequest;
import com.kt.domain.dto.response.OrderResponse;
import com.kt.domain.entity.CategoryEntity;
import com.kt.domain.entity.OrderEntity;
import com.kt.domain.entity.OrderProductEntity;
import com.kt.domain.entity.ProductEntity;
import com.kt.domain.entity.ReceiverVO;
import com.kt.domain.entity.UserEntity;
import com.kt.exception.BaseException;
import com.kt.repository.CategoryRepository;
import com.kt.repository.OrderProductRepository;
import com.kt.repository.OrderRepository;
import com.kt.repository.ProductRepository;
import com.kt.repository.ReviewRepository;
import com.kt.repository.user.UserRepository;

@Transactional
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceTest {

	@Autowired
	private OrderService orderService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderProductRepository orderProductRepository;
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private CategoryRepository categoryRepository;

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

	@Test
	void 주문_생성_성공() {
		// given
		String email = "test@example.com";

		UserEntity savedUser = userRepository.save(
			UserEntity.create(
				"김도현",
				"test@example.com",
				"111",
				UserRole.MEMBER,
				Gender.MALE,
				LocalDate.now(),
				"0101010"
			)
		);

		ProductEntity product1 = ProductEntity.create("상품1", 10L, 10000L, defaultCategory);
		productRepository.save(product1);

		ProductEntity product2 = ProductEntity.create("상품2", 5L, 20000L, defaultCategory);
		productRepository.save(product2);

		OrderRequest.Item item1 = new OrderRequest.Item(product1.getId(), 2L);
		OrderRequest.Item item2 = new OrderRequest.Item(product2.getId(), 1L);

		List<OrderRequest.Item> items = List.of(item1, item2);

		// when
		orderService.createOrder(email, items);

		// then
		List<OrderEntity> orders = orderRepository.findAll();

		assertThat(orders).isNotEmpty();
		OrderEntity savedOrder = orders.get(orders.size() - 1);

		List<OrderProductEntity> orderProducts =
			orderProductRepository.findAll()
				.stream()
				.filter(orderProductEntity -> orderProductEntity.getOrder().getId().equals(savedOrder.getId()))
				.toList();

		assertThat(orderProducts).hasSize(2);
		assertThat(orderProducts.get(0).getQuantity()).isGreaterThan(0);
		assertThat(orderProducts.get(1).getQuantity()).isGreaterThan(0);
	}

	@Test
	void 주문_생성_실패__상품없음() {
		// given

		String email = "test@example.com";
		UserEntity user = UserEntity.create(
			"김도현",
			"test@example.com",
			"111",
			UserRole.MEMBER,
			Gender.MALE,
			LocalDate.now(),
			"0101010"
		);

		UserEntity savedEntity = userRepository.save(user);

		UUID invalidProductId = UUID.fromString("11111111-2222-3333-4444-555555555555");

		List<OrderRequest.Item> items = List.of(
			new OrderRequest.Item(invalidProductId, 1L)
		);

		// when, then
		assertThatThrownBy(() -> orderService.createOrder(email, items))
			.isInstanceOf(BaseException.class)
			.hasMessageContaining("PRODUCT_NOT_FOUND");
	}

	@Test
	void 주문_생성_실패__재고부족() {

		// given
		UserEntity user = UserEntity.create(
			"김도현",
			"test@example.com",
			"111",
			UserRole.MEMBER,
			Gender.MALE,
			LocalDate.now(),
			"0101010"
		);

		ProductEntity product = productRepository.save(
			ProductEntity.create("상품1", 3L, 1L, defaultCategory)
		);

		UserEntity savedUser = userRepository.save(user);
		ProductEntity savedProduct = productRepository.save(product);
		// given

		List<OrderRequest.Item> items = List.of(
			new OrderRequest.Item(savedProduct.getId(), 2L)
		);

		// then
		assertThatThrownBy(() -> orderService.createOrder(savedUser.getEmail(), items))
			.isInstanceOf(BaseException.class)
			.hasMessageContaining("STOCK_NOT_ENOUGH");
	}

	@Test
	void 주문상품_조회() {
		// given
		UserEntity user = UserEntity.create(
			"김도현",
			"test@example.com",
			"111",
			UserRole.MEMBER,
			Gender.MALE,
			LocalDate.now(),
			"0101010"
		);
		UserEntity savedUser = userRepository.save(user);

		ProductEntity product = ProductEntity.create("테스트 상품", 5L, 10000L, defaultCategory);
		ProductEntity savedProduct = productRepository.save(product);

		OrderEntity order = OrderEntity.create(
			ReceiverVO.create("이름", "번호", "도시", "시군구", "도로명", "상세"),
			savedUser
		);
		OrderEntity savedOrder = orderRepository.save(order);
		OrderProductEntity orderProduct = OrderProductEntity.create(
			2L,
			10000L,
			OrderProductStatus.CREATED,
			savedOrder,
			savedProduct
		);
		OrderProductEntity savedOrderProduct = orderProductRepository.save(orderProduct);

		// when
		OrderResponse.OrderProducts foundOrderProduct = orderService.getOrderProducts(savedOrder.getId());

		// then
		assertThat(foundOrderProduct).isNotNull();
		assertThat(foundOrderProduct.orderId()).isEqualTo(savedOrder.getId());
		assertThat(foundOrderProduct.orderProducts()).isNotEmpty();
		assertThat(foundOrderProduct.orderProducts().size()).isEqualTo(1);
	}

	@Test
	void 주문_취소_성공() {
		// given
		long initialStock = 10000L;
		long orderQuantity = 3L;

		UserEntity user = userRepository.save(
			UserEntity.create("유저5", "cancel_test@example.com", "111", UserRole.MEMBER, Gender.MALE, LocalDate.now(), "0101010")
		);

		ProductEntity product = productRepository.save(
			ProductEntity.create("취소상품", 10000L, initialStock, ProductStatus.ACTIVATED, defaultCategory)
		);

		OrderEntity order = orderRepository.save(
			OrderEntity.create(ReceiverVO.create("이름", "번호", "도시", "시군구", "도로명", "상세"), user, OrderStatus.CREATED)
		);

		product.decreaseStock(orderQuantity);
		productRepository.save(product);

		OrderProductEntity orderProduct = orderProductRepository.save(
			OrderProductEntity.create(
				orderQuantity, 10000L, OrderProductStatus.CREATED, order, product
			)
		);

		UUID orderId = order.getId();

		// when
		orderService.cancelOrder(orderId);

		// then
		OrderProductEntity canceledOrderProduct = orderProductRepository.findById(orderProduct.getId())
			.orElseThrow(() -> new IllegalStateException("취소된 주문 상품을 찾을 수 없습니다."));
		assertThat(canceledOrderProduct.getStatus()).isEqualTo(OrderProductStatus.CANCELED);

		ProductEntity finalProduct = productRepository.findById(product.getId())
			.orElseThrow(() -> new IllegalStateException("상품을 찾을 수 없습니다."));
		assertThat(finalProduct.getStock()).isEqualTo(initialStock);

	}

	@Test
	void 주문_취소_실패__이미_처리됨() {
		// given

		UserEntity user = userRepository.save(
			UserEntity.create("유저", "cancel_fail@example.com", "111", UserRole.MEMBER, Gender.MALE, LocalDate.now(), "0101010")
		);

		OrderEntity order = orderRepository.save(
			OrderEntity.create(
				ReceiverVO.create("이름", "번호", "도시", "시군구", "도로명", "상세"),
				user,
				OrderStatus.PURCHASE_CONFIRMED
			)
		);
		UUID orderId = order.getId();

		// when, then
		assertThatThrownBy(() -> orderService.cancelOrder(orderId))
			.isInstanceOf(BaseException.class)
			.hasMessageContaining("ORDER_ALREADY_SHIPPED");
	}

	@Test
	void 주문_수정_성공__배송정보_변경() {
		// given
		long initialStock = 10000L;

		UserEntity user = userRepository.save(
			UserEntity.create("유저", "update_test@example.com", "111", UserRole.MEMBER, Gender.MALE, LocalDate.now(), "0101010")
		);

		ProductEntity product = productRepository.save(
			ProductEntity.create("기존 상품", 10000L, initialStock, ProductStatus.ACTIVATED, defaultCategory)
		);

		OrderEntity order = orderRepository.save(
			OrderEntity.create(ReceiverVO.create("김기존", "01011112222", "서울시", "강남구", "역삼로", "1층"), user, OrderStatus.CREATED)
		);
		product.decreaseStock(2L);
		productRepository.save(product);
		orderProductRepository.save(
			OrderProductEntity.create(2L, 10000L, OrderProductStatus.CREATED, order, product)
		);

		OrderRequest.Update updateRequest = new OrderRequest.Update(
			"박수정",
			"01099998888",
			"서울특별시",
			"강동구",
			"김김대로",
			"2층"
		);

		UUID orderId = order.getId();

		// when
		orderService.updateOrder(orderId, updateRequest);

		// then
		OrderEntity updatedOrder = orderRepository.findById(orderId).get();

		assertThat(updatedOrder.getReceiverVO().getName()).isEqualTo("박수정");
		assertThat(updatedOrder.getReceiverVO().getMobile()).isEqualTo("01099998888");

		ProductEntity finalProduct = productRepository.findById(product.getId()).get();
		assertThat(finalProduct.getStock()).isEqualTo(initialStock - 2L);
	}

	@Test
	void 주문_수정_실패__이미_처리됨() {
		// given
		UserEntity user = userRepository.save(
			UserEntity.create("유저", "update_fail@example.com", "111", UserRole.MEMBER, Gender.MALE, LocalDate.now(), "0101010")
		);

		OrderEntity order = orderRepository.save(
			OrderEntity.create(
				ReceiverVO.create("이름", "번호", "도시", "시군구", "도로명", "상세"),
				user,
				OrderStatus.PURCHASE_CONFIRMED
			)
		);

		UUID orderId = order.getId();

		OrderRequest.Update updateRequest = new OrderRequest.Update(
			"박수정", "01099998888", "부산시", "해운대구", "센텀대로", "2층"
		);

		// when, then
		assertThatThrownBy(() -> orderService.updateOrder(orderId, updateRequest))
			.isInstanceOf(BaseException.class)
			.hasMessageContaining("ORDER_ALREADY_SHIPPED");
	}

}
