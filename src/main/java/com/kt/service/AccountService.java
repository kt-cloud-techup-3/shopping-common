package com.kt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kt.constant.CourierWorkStatus;
import com.kt.constant.UserRole;

import java.util.UUID;

public interface AccountService {
	Page<?> searchAccounts(
		Pageable pageable,
		String keyword,
		UserRole role,
		CourierWorkStatus workStatus
	);

	void adminResetAccountPassword(UUID accountId);
}
