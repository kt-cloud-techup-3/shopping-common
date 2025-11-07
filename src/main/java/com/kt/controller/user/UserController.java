package com.kt.controller.user;

import com.kt.common.ApiResult;
import com.kt.common.SwaggerAssistance;
import com.kt.dto.user.UserRequest;
import com.kt.dto.user.UserUpdatePasswordRequest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kt.service.UserService;

import lombok.RequiredArgsConstructor;

@Tag(name = "유저", description = "유저 관련 API")
@RequestMapping("/users")
@RequiredArgsConstructor
@RestController
public class UserController extends SwaggerAssistance {

	private final UserService userService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResult<Void> create(@Valid @RequestBody UserRequest.Create request) {
		userService.create(request);
		return ApiResult.ok();
	}

	@GetMapping("/duplicate-login-id")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Boolean> isDuplicateLoginId(@RequestParam String loginId) {
		var result = userService.isDuplicateLoginId(loginId);

		return ApiResult.ok(result);
	}

	@PutMapping("/{id}/update-password")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> updatePassword(
		@PathVariable Long id,
		@RequestBody @Valid UserUpdatePasswordRequest request
	) {
		userService.changePassword(id, request.oldPassword(), request.newPassword());
		return ApiResult.ok();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> delete(@PathVariable Long id) {
		userService.delete(id);
		return ApiResult.ok();
	}

}
