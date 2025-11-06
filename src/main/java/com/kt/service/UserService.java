package com.kt.service;

import com.kt.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kt.domain.user.User;
import com.kt.dto.UserCreateRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public void create(UserCreateRequest request) {
		User user = new User(
			request.loginId(),
			request.password(),
			request.name(),
			request.email(),
			request.mobile(),
			request.gender(),
			request.birthday(),
			LocalDateTime.now(),
			LocalDateTime.now()
		);
		userRepository.save(user);
	}

	public boolean isDuplicateLoginId(String loginId) {
		return userRepository.existsByLoginId(loginId);
	}

	@Transactional
	public void changePassword(Long id, String oldPassword, String newPassword) {
		User user = userRepository.findById(id).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 회원입니다.")
		);

		if(!user.getPassword().equals(oldPassword)) {
			throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
		}
		user.updatePassword(newPassword);
	}

	public Page<User> search(Pageable pageable, String keyword) {
		Page<User> users = userRepository.findAllByNameContaining(keyword, pageable);
		return users;
	}

	public User detail(Long id) {
		return userRepository.findById(id).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 회원입니다.")
		);
	}

	@Transactional
	public void update(Long id, String name, String email, String mobile) {
		User user = userRepository.findById(id).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 회원입니다.")
		);

		user.update(name, email, mobile);
	}

}
