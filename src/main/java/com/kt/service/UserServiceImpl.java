package com.kt.service;

import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.constant.message.ErrorCode;
import com.kt.domain.dto.request.UserRequest;
import com.kt.domain.dto.response.OrderProductResponse;
import com.kt.domain.dto.response.UserResponse;
import com.kt.domain.entity.OrderEntity;
import com.kt.domain.entity.UserEntity;
import com.kt.exception.AuthException;
import com.kt.exception.DuplicatedException;
import com.kt.repository.OrderProductRepository;
import com.kt.repository.OrderRepository;
import com.kt.repository.UserRepository;
import com.kt.util.ValidationUtil;

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
	public void updatePassword(
		UUID userId,
		String currentPassword ,
		String newPassword)
	{
		UserEntity user = userRepository.findByUserIdOrThrow(userId);

		if ( newPassword.equals(currentPassword) )
			throw new DuplicatedException(ErrorCode.DUPLICATED_PASSWORD);

		if ( !passwordEncoder.matches(currentPassword,user.getPassword()) )
			throw new AuthException(ErrorCode.NOT_CORRECT_PASSWORD);

		String hashedPassword = passwordEncoder.encode(newPassword);
		user.updatePassword(hashedPassword);
	}

	@Override
	public void delete(UUID userId) {
		UserEntity user = userRepository.findByUserIdOrThrow(userId);
		user.delete();
	}

	@Override
	public List<OrderProductResponse.SearchReviewable> getReviewableOrderProducts(UUID userId) {
		return orderProductRepository.getReviewableOrderProductsByUserId(userId);
	}

	@Override
	public UserResponse.UserDetail getUser(UUID userId){
		UserEntity user = userRepository.findByUserIdOrThrow(userId);
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
	public void updateUserDetails(UUID userId, UserRequest.UpdateDetails details) {
		UserEntity user = userRepository.findByUserIdOrThrow(userId);
		user.updateDetails(
			details.name(),
			details.mobile(),
			details.birth(),
			details.gender()
		);
	}
}
