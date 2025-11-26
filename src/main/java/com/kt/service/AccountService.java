package com.kt.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kt.constant.CourierWorkStatus;
import com.kt.constant.UserRole;
import com.kt.domain.dto.response.AccountResponse;

public interface AccountService {
	Page<?> searchAccounts(
		Pageable pageable,
		String keyword,
		UserRole role,
		CourierWorkStatus workStatus
	);

	void adminResetAccountPassword(UUID accountId);

	void updatePassword(
		UUID accountId,
		String currentPassword,
		String newPassword
	);

	void deleteAccount(UUID accountId);

	AccountResponse.search getAccount(UUID accoundId);
}
