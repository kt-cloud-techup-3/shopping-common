package com.kt.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.kt.domain.dto.request.OrderRequest;
import com.kt.domain.entity.OrderEntity;
import com.kt.domain.entity.OrderProductEntity;
import com.kt.exception.BaseException;
import com.kt.repository.OrderProductRepository;
import com.kt.repository.OrderRepository;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderServiceTest {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderProductRepository orderProductRepository;

	@Test
	void 주문_생성_성공() {
		// given
		String email = "test@example.com";

		UUID productId1 = UUID.fromString("dc3f261a-c60f-11f0-b06b-622bb1d1e593");
		UUID productId2 = UUID.fromString("dc3f37ad-c60f-11f0-b06b-622bb1d1e593");

		OrderRequest.Item item1 = new OrderRequest.Item(productId1, 2L);
		OrderRequest.Item item2 = new OrderRequest.Item(productId2, 1L);

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
		String email = "test@example.com";

		UUID productId = UUID.fromString("dc3f39e9-c60f-11f0-b06b-622bb1d1e593");

		List<OrderRequest.Item> items = List.of(
			new OrderRequest.Item(productId, 9999L)
		);

		// when, then
		assertThatThrownBy(() -> orderService.createOrder(email, items))
			.isInstanceOf(BaseException.class)
			.hasMessageContaining("STOCK_NOT_ENOUGH");
	}




}
