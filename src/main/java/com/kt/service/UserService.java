package com.kt.service;

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
		var newUser = new User(
			request.loginId(),
			request.password(),
			request.name(),
			request.birthday()
		);

		userRepository.save(newUser);
		//repository로 넘길거임
	}
}
