package com.kt.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kt.constant.UserRole;
import com.kt.domain.dto.response.OrderProductResponse;
import com.kt.domain.dto.response.UserResponse;

public interface UserService {
	List<OrderProductResponse.SearchReviewable> getReviewableOrderProducts(UUID userId);

	UserResponse.Orders getOrdersByUserId(UUID id);

	Page<UserResponse.Search> getUsers(Pageable pageable, String name, UserRole role);

	UserResponse.UserDetail getUserDetail(UUID id);

	void disableUser(UUID id);

	void enableUser(UUID id);

	void deleteUser(UUID id);

	void retireUser(UUID id);
}
