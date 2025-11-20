package com.kt.service;

import java.util.UUID;

import com.kt.domain.dto.response.UserResponse;

import java.util.List;
import com.kt.dto.response.OrderProductResponse;

public interface UserService{
	List<OrderProductResponse.SearchReviewable> getReviewableOrderProducts(UUID userId);
public interface UserService {
	UserResponse.Orders getOrdersByUserId(UUID id);
}
