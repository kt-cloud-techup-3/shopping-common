package com.kt.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kt.constant.OrderStatus;
import com.kt.constant.message.ErrorCode;
import com.kt.domain.dto.response.AdminOrderResponse;
import com.kt.domain.entity.OrderEntity;
import com.kt.domain.entity.OrderProductEntity;
import com.kt.exception.BaseException;
import com.kt.repository.orderproduct.OrderProductRepository;
import com.kt.repository.OrderRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminOrderServiceImpl implements AdminOrderService {

	private final OrderRepository orderRepository;
	private final OrderProductRepository orderProductRepository;

	@Override
	public List<AdminOrderResponse.Search> getOrders() {
		return orderRepository.findAll().stream()
			.map(AdminOrderResponse.Search::from)
			.toList();
	}

	@Override
	public AdminOrderResponse.Detail getOrder(UUID orderId) {
		OrderEntity order = orderRepository.findById(orderId)
			.orElseThrow(() -> new BaseException(ErrorCode.ORDER_NOT_FOUND));

		List<OrderProductEntity> products =
			orderProductRepository.findAllByOrderId(order.getId());

		return AdminOrderResponse.Detail.from(order, products);
	}

	@Override
	@Transactional
	public void changeStatus(UUID orderId, OrderStatus newStatus) {
		OrderEntity order = orderRepository.findById(orderId)
			.orElseThrow(() -> new BaseException(ErrorCode.ORDER_NOT_FOUND));

		order.changeStatus(newStatus);
	}

	@Override
	@Transactional
	public void cancelOrder(UUID orderId) {
		
		OrderEntity order = orderRepository.findById(orderId)
			.orElseThrow(() -> new BaseException(ErrorCode.ORDER_NOT_FOUND));
		
		List<OrderProductEntity> orderProducts =
			orderProductRepository.findAllByOrderId(orderId);
		
		for (OrderProductEntity orderproduct : orderProducts) {
			orderproduct.getProduct().addStock(orderproduct.getQuantity());
			orderproduct.cancel();                               
		}
		
		order.cancel();
	}

}
