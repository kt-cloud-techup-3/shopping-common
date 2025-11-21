package com.kt.service;

import java.util.List;
import java.util.UUID;

import com.kt.domain.dto.response.UserResponse;
import com.kt.dto.response.OrderProductResponse;

public interface UserService {
	List<OrderProductResponse.SearchReviewable> getReviewableOrderProducts(UUID userId);

	UserResponse.Orders getOrdersByUserId(UUID id);
}
