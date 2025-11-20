package com.kt.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.constant.OrderProductStatus;
import com.kt.constant.message.ErrorCode;
import com.kt.domain.dto.response.OrderResponse;
import com.kt.domain.dto.request.OrderRequest;
import com.kt.domain.entity.ProductEntity;
import com.kt.domain.entity.UserEntity;
import com.kt.domain.entity.OrderEntity;
import com.kt.domain.entity.OrderProductEntity;
import com.kt.domain.entity.ReceiverVO;
import com.kt.exception.BaseException;
import com.kt.repository.ProductRepository;
import com.kt.repository.UserRepository;
import com.kt.repository.OrderRepository;
import com.kt.repository.OrderProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final OrderProductRepository orderProductRepository;

	@Override
	public OrderResponse.OrderProducts getOrderProducts(UUID orderId) {
		List<OrderProductEntity> orderProducts = orderProductRepository.findAllByOrder_Id(orderId).orElseThrow(() ->
			new BaseException(ErrorCode.ORDER_NOT_FOUND)
		);

		return OrderResponse.OrderProducts.of(orderId, orderProducts);
	}

	@Override
	public void createOrder(String email, List<OrderRequest.Item> items) {

		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

		// TODO: 컨트롤러 구현
		ReceiverVO receiverVO = ReceiverVO.create(
			"홍길동",
			"010-1234-5678",
			"서울특별시",
			"강남구",
			"테헤란로",
			"111호"
		);

		OrderEntity order = OrderEntity.create(receiverVO, user);
		orderRepository.save(order);

		for (OrderRequest.Item item : items) {

			UUID productId = item.productId();
			Long quantity = item.quantity();

			ProductEntity product = productRepository.findByIdOrThrow(productId);

			if (product.getStock() < quantity) {
				throw new BaseException(ErrorCode.STOCK_NOT_ENOUGH);
			}

			OrderProductEntity orderProduct = new OrderProductEntity(
				quantity,
				product.getPrice(),
				OrderProductStatus.CREATED,
				order,
				product
			);

			orderProductRepository.save(orderProduct);
		}
	}

}