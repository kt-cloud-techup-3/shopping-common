package com.kt.controller.account;

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
	public ResponseEntity<ApiResult<UserResponse.Detail>> getMemberBySelf(
		@AuthenticationPrincipal DefaultCurrentUser defaultCurrentUser
	){
		UserResponse.Detail memberResponse = userService.getMemberDetail(
			defaultCurrentUser.getId(),
			defaultCurrentUser.getRole()
		);
		return ApiResult.ok(memberResponse);
	}

	@PutMapping
	public ResponseEntity<ApiResult<Void>> updateMemberBySelf(
		@AuthenticationPrincipal DefaultCurrentUser defaultCurrentUser,
		@RequestBody @Valid UserRequest.UpdateDetails request
	){
		userService.updateMemberDetails(
			defaultCurrentUser.getId(),
			defaultCurrentUser.getRole(),
			request
		);
		return ApiResult.ok(null);
	}
}
