package com.kt.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.kt.domain.User;
import com.kt.dto.UserCreateRequest;
import com.kt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public void create(UserCreateRequest request) {
		System.out.println(request.toString());
		var maxId = userRepository.findMaxId() == null ? 0L : userRepository.findMaxId();
		var newUser = new User(
			maxId + 1,
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
		//repository로 넘길거임
	}
}
