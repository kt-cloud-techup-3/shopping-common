package com.kt.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.kt.domain.dto.request.AccountSearchRequestVO;

public interface AccountService {
	Page<?> searchAccounts(
		Pageable pageable,
		AccountSearchRequestVO accountSearchRequestVO
	);

	void adminResetAccountPassword(UUID accountId);

	void updatePassword(
		UUID accountId,
		String currentPassword,
		String newPassword
	);

	void deleteAccount(UUID accountId);
}
