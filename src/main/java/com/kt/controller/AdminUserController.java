package com.kt.controller;

import com.kt.domain.User;
import com.kt.dto.CustomPage;

import com.kt.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/admin/users")
@RestController
public class AdminUserController {

	private final UserService userService;

	// 유저 리스트 조회
	//?key=value&page=1&keyword=asdasd
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public CustomPage search(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String keyword) {
		return userService.search(page, size, keyword);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public User detail(@PathVariable Long id) {
		return userService.detail(id);
	}
	

}
