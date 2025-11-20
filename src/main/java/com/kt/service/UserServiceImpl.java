package com.kt.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.domain.entity.OrderProductEntity;
import com.kt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public List<OrderProductEntity> getReviewableOrderProducts(UUID userId) {
		return userRepository.getReviewtableOrderProducts(userId);
	}
}