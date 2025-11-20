package com.kt.service;

import com.kt.constant.UserRole;
import com.kt.constant.message.ErrorCode;
import com.kt.domain.dto.request.LoginRequest;
import com.kt.domain.dto.request.MemberRequest;

import com.kt.domain.entity.AbstractAccountEntity;
import com.kt.domain.entity.UserEntity;

import com.kt.exception.AuthException;
import com.kt.exception.DuplicatedException;
import com.kt.repository.AccountRepository;
import com.kt.repository.UserRepository;

import com.kt.security.JwtService;
import com.mysema.commons.lang.Pair;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;

	private final AccountRepository accountRepository;

	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	@Override
	@Transactional
	public void memberSignup(MemberRequest.SignupMember request) {
		isDuplicatedEmail(request.email());
		UserEntity member = UserEntity.create(
			request.name(),
			request.email(),
			passwordEncoder.encode(request.password()),
			UserRole.MEMBER,
			request.gender(),
			request.birth(),
			request.mobile()
		);
		userRepository.save(member);
	}

	@Override
	public Pair<String, String> login(LoginRequest request) {

		AbstractAccountEntity account = accountRepository.findByEmail(request.email()).orElseThrow(
			() -> new AuthException(ErrorCode.AUTH_FAILED_LOGIN)
		);

		validAccount(account, request.password());

		String accessToken = jwtService.issue(
			account.getId(),
			account.getEmail(),
			account.getRole(),
			jwtService.getAccessExpiration()
		);

		String refreshToken = jwtService.issue(
			account.getId(),
			account.getEmail(),
			account.getRole(),
			jwtService.getRefreshExpiration()
		);

		return Pair.of(accessToken, refreshToken);
	}

	private void validAccount(AbstractAccountEntity account, String rawPassword) {
		if (!passwordEncoder.matches(rawPassword, account.getPassword()))
			throw new AuthException(ErrorCode.AUTH_FAILED_LOGIN);

		switch (account.getStatus()) {
			case DELETED -> throw new AuthException(ErrorCode.AUTH_ACCOUNT_DELETED);
			case DISABLED -> throw new AuthException(ErrorCode.AUTH_ACCOUNT_DISABLED);
			case RETIRED -> throw new AuthException(ErrorCode.AUTH_ACCOUNT_RETIRED);
		}
	}

	private void isDuplicatedEmail(String email) {
		if (userRepository.findByEmail(email).isPresent())
			throw new DuplicatedException(ErrorCode.DUPLICATED_EMAIL);
	}

}
