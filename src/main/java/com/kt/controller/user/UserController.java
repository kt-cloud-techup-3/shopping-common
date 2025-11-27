package com.kt.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.api.ApiResult;
import com.kt.domain.dto.request.UserRequest;
import com.kt.domain.dto.response.UserResponse;
import com.kt.security.DefaultCurrentUser;
import com.kt.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@GetMapping
	public ResponseEntity<ApiResult<UserResponse.UserDetail>> getUserBySelf(
		@AuthenticationPrincipal DefaultCurrentUser defaultCurrentUser
	){
		UserResponse.UserDetail userResponse = userService.getUserDetail(
			defaultCurrentUser.getId()
		);
		return ApiResult.ok(userResponse);
	}

	@PutMapping
	public ResponseEntity<ApiResult<Void>> updateUserBySelf(
		@AuthenticationPrincipal DefaultCurrentUser defaultCurrentUser,
		@RequestBody @Valid UserRequest.UpdateDetails request
	){
		userService.updateUserDetail(
			defaultCurrentUser.getId(),
			request
		);
		return ApiResult.ok(null);
	}
}
