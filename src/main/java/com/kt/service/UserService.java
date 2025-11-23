package com.kt.service;

import java.util.List;
import java.util.UUID;

import com.kt.domain.dto.response.OrderProductResponse;
import com.kt.domain.dto.response.UserResponse;

public interface UserService {
	List<OrderProductResponse.SearchReviewable> getReviewableOrderProducts(UUID userId);

	UserResponse.Orders getOrdersByUserId(UUID id);

	void updatePassword(UUID userId, String oldPassword , String newPassword);

	void delete(UUID userId);
}
