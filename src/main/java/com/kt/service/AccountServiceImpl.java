package com.kt.service;

import com.kt.constant.mail.MailTemplate;
import com.kt.domain.entity.AbstractAccountEntity;

import com.kt.infra.mail.EmailClient;
import com.kt.repository.AccountRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kt.constant.CourierWorkStatus;
import com.kt.constant.UserRole;
import com.kt.repository.courier.CourierRepository;
import com.kt.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
	private final UserRepository userRepository;
	private final CourierRepository courierRepository;
	private final AccountRepository accountRepository;
	private final EmailClient emailClient;
	private final PasswordEncoder passwordEncoder;

	@Override
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

	@Override
	public void adminResetAccountPassword(UUID accountId) {
		AbstractAccountEntity account = accountRepository.findByIdOrThrow(accountId);
		String resetPassword = getRandomPassword();
		account.resetPassword(passwordEncoder.encode(resetPassword));
		emailClient.sendMail(
			account.getEmail(),
			MailTemplate.RESET_PASSWORD,
			resetPassword
		);
	}

	private String getRandomPassword() {
		int code = new Random().nextInt(900000) + 100000;
		return String.valueOf(code);
	}
}
