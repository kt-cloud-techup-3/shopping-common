package com.kt.service;

import com.kt.constant.UserRole;
import com.kt.domain.dto.request.MemberRequest;

import com.kt.domain.entity.UserEntity;

import com.kt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

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

	private void isDuplicatedEmail(String email) {
		if (userRepository.findByEmail(email).isPresent())
			throw new IllegalArgumentException("중복된 이메일입니다");
	}

}
