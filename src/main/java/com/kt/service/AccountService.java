package com.kt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kt.constant.CourierWorkStatus;
import com.kt.constant.UserRole;
import com.kt.repository.courier.CourierRepository;
import com.kt.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	private final UserRepository userRepository;
	private final CourierRepository courierRepository;

	public Page<?> searchAccounts(
		Pageable pageable,
		String keyword,
		UserRole role,
		CourierWorkStatus workStatus
	) {
		if (role == UserRole.COURIER) {
			return courierRepository.searchCouriers(pageable, keyword, workStatus);
		}
		return userRepository.searchUsers(pageable, keyword, role);
	}
}
