package com.kt.controller;

import com.kt.domain.user.User;

import com.kt.dto.UserUpdateRequest;
import com.kt.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public Page<User> search(
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(required = false) String keyword) {
		return userService.search(PageRequest.of(page - 1 , size), keyword);
	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public User detail(@PathVariable Long id) {
		return userService.detail(id);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void update(
		@PathVariable Long id,
		@RequestBody @Valid UserUpdateRequest request) {
		userService.update(id, request.name(), request.email(), request.mobile());
	}



}
