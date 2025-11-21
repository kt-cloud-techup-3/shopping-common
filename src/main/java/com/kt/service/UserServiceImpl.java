package com.kt.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.dto.response.OrderProductResponse;
import com.kt.repository.OrderProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final OrderProductRepository orderProductRepository;

	@Override
	public List<OrderProductResponse.SearchReviewable> getReviewableOrderProducts(UUID userId) {
		return orderProductRepository.getReviewableOrderProductsByUserId(userId);
	}
}