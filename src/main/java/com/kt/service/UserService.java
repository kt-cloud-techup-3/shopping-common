package com.kt.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kt.constant.UserRole;
import com.kt.domain.dto.request.UserRequest;
import com.kt.domain.dto.request.SignupRequest;
import com.kt.domain.dto.response.OrderProductResponse;
import com.kt.domain.dto.response.UserResponse;

public interface UserService {
	List<OrderProductResponse.SearchReviewable> getReviewableOrderProducts(UUID userId);

	UserResponse.Orders getOrdersByUserId(UUID id);

	Page<UserResponse.Search> getUsers(Pageable pageable, String keyword, UserRole role);

	UserResponse.Detail getMemberDetail(UUID id, UserRole role);

	UserResponse.Detail getAdminDetail(UUID id, UserRole role);

	void disableUser(UUID id);

	void enableUser(UUID id);

	void deleteUser(UUID id);

	void deleteUserPermanently(UUID id);

	void retireUser(UUID id);

	void createAdmin(SignupRequest.SignupMember request);

	void deleteAdmin(UUID adminId);

	void updateMemberDetails(UUID userId, UserRole userRole, UserRequest.UpdateDetails details);
}
