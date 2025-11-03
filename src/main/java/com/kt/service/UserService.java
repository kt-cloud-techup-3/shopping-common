package com.kt.service;

import com.kt.dto.CustomPage;

import org.springframework.stereotype.Service;

import com.kt.domain.User;
import com.kt.dto.UserCreateRequest;
import com.kt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public void create(UserCreateRequest request) {
		System.out.println(request.toString());
		var newUser = new User(
			userRepository.selectMaxId() + 1,
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

		userRepository.save(newUser);
	}

	public boolean isDuplicateLoginId(String loginId) {
		return userRepository.existsByLoginId(loginId);
	}

	public void changePassword(int id, String oldPassword, String password) {

		var user = userRepository.selectById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

		if(!user.getPassword().equals(oldPassword)) {
			throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
		}

		userRepository.updatePassword(id, password);
	}

	public CustomPage search(int page, int size, String keyword) {
		var pair = userRepository.selectAll(page - 1, size, keyword);
		var pages = (int) Math.ceil((double) pair.getSecond() / size);

		return new CustomPage(
			pair.getFirst(),
			size,
			page,
			pages,
			pair.getSecond()
		);
	}

	public User detail(Long id) {
		return userRepository.selectById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
	}

	public void update(Long id, String name, String email, String mobile) {
		userRepository.selectById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

		userRepository.updateById(id, name, email, mobile);
	}

}
