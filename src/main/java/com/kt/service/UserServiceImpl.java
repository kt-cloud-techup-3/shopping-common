package com.kt.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.dto.response.OrderProductResponse;
import com.kt.repository.OrderProductRepository;
import com.kt.domain.dto.response.UserResponse;
import com.kt.domain.entity.OrderEntity;
import com.kt.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final OrderProductRepository orderProductRepository;
	private final OrderRepository orderRepository;

	public UserResponse.Orders getOrdersByUserId(UUID id) {
		List<OrderEntity> orders = orderRepository.findAllByOrderBy_Id(id);

		return UserResponse.Orders.of(id, orders);
	}

	@Override
	public List<OrderProductResponse.SearchReviewable> getReviewableOrderProducts(UUID userId) {
		return orderProductRepository.getReviewableOrderProductsByUserId(userId);
	}
}
