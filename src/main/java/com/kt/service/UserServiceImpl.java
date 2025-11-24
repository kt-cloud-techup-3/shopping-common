package com.kt.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.constant.UserRole;
import com.kt.domain.dto.request.SignupRequest;
import com.kt.domain.dto.response.OrderProductResponse;
import com.kt.domain.dto.response.UserResponse;
import com.kt.domain.entity.OrderEntity;
import com.kt.domain.entity.UserEntity;
import com.kt.repository.OrderProductRepository;
import com.kt.repository.OrderRepository;
import com.kt.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final OrderProductRepository orderProductRepository;
	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserResponse.Orders getOrdersByUserId(UUID id) {
		List<OrderEntity> orders = orderRepository.findAllByOrderBy_Id(id);
		return UserResponse.Orders.of(id, orders);
	}

	@Override
	public List<OrderProductResponse.SearchReviewable> getReviewableOrderProducts(UUID userId) {
		return orderProductRepository.getReviewableOrderProductsByUserId(userId);
	}

	@Override
	public Page<UserResponse.Search> getUsers(Pageable pageable, String keyword, UserRole role) {
		return userRepository.searchUsers(pageable, keyword, role);
	}

	@Override
	public UserResponse.UserDetail getUserDetail(UUID id) {
		UserEntity user = userRepository.findByIdOrThrow(id);
		return new UserResponse.UserDetail(
			user.getId(),
			user.getName(),
			user.getEmail(),
			user.getRole(),
			user.getGender(),
			user.getBirth(),
			user.getMobile()
		);
	}

	@Override
	public UserResponse.UserDetail getAdminDetail(UUID id) {
		UserEntity user = userRepository.findByIdOrThrow(id);
		return new UserResponse.UserDetail(
			user.getId(),
			user.getName(),
			user.getEmail(),
			user.getRole(),
			user.getGender(),
			user.getBirth(),
			user.getMobile()
		);
	}

	@Override
	public void enableUser(UUID id) {
		UserEntity user = userRepository.findByIdOrThrow(id);
		user.enabled();
	}

	@Override
	public void disableUser(UUID id) {
		UserEntity user = userRepository.findByIdOrThrow(id);
		user.disabled();
	}

	@Override
	public void deleteUser(UUID id) {
		UserEntity user = userRepository.findByIdOrThrow(id);
		user.delete();
	}

	@Override
	public void retireUser(UUID id) {
		UserEntity user = userRepository.findByIdOrThrow(id);
		user.retired();
	}

	@Override
	public void signupMember(SignupRequest.SignupMember request) {
		UserEntity admin = UserEntity.create(
			request.name(),
			request.email(),
			passwordEncoder.encode(request.password()),
			UserRole.ADMIN,
			request.gender(),
			request.birth(),
			request.mobile()
		);
		userRepository.save(admin);
	}

}
