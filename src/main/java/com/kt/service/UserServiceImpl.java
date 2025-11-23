package com.kt.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.domain.dto.request.UserRequest;
import com.kt.domain.dto.response.OrderProductResponse;
import com.kt.domain.dto.response.UserResponse;
import com.kt.domain.entity.OrderEntity;
import com.kt.domain.entity.UserEntity;
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
		ValidationUtil.validateSameString(currentPassword, newPassword, "비밀번호");
		ValidationUtil.validateCollectPassword(newPassword, "비밀번호");

		UserEntity user = userRepository.findByUserIdOrThrow(userId);
		user.updatePassword(newPassword);
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
	public UserResponse.Details getUser(UUID userId){
		UserEntity user = userRepository.findByUserIdOrThrow(userId);
		return new UserResponse.Details(
			user.getId(),
			user.getName(),
			user.getMobile(),
			user.getBirth(),
			user.getGender(),
			user.getRole(),
			user.getStatus()
		);
	}
}
