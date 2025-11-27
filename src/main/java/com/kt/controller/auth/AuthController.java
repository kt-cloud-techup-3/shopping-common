package com.kt.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.api.ApiResult;
import com.kt.domain.dto.request.SignupRequest;
import com.kt.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/email/code")
	public ResponseEntity<ApiResult<Void>> sendAuthCode(
		@RequestBody @Valid SignupRequest.SignupEmail request
	) {
		authService.sendAuthCode(request);
		return ApiResult.ok();
	}

	@PostMapping("/email/verify")
	public ResponseEntity<ApiResult<Void>> verifySignupCode(
		@RequestBody @Valid SignupRequest.VerifySignupCode request
	) {
		authService.verifySignupCode(request);
		return ApiResult.ok();
	}

	@PostMapping("/signup/member")
	public ResponseEntity<ApiResult<Void>> signupMember(
		@RequestBody @Valid SignupRequest.SignupMember request
	) {
		authService.signupMember(request);
		return ApiResult.ok();
	}

	@PostMapping("/signup/courier")
	public ResponseEntity<ApiResult<Void>> signupCourier(
		@RequestBody @Valid SignupRequest.SignupCourier request
	) {
		authService.signupCourier(request);
		return ApiResult.ok();
	}
}
