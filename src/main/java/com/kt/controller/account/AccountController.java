package com.kt.controller.account;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.api.ApiResult;
import com.kt.domain.dto.request.AccountRequest;
import com.kt.domain.dto.response.AccountResponse;
import com.kt.security.DefaultCurrentUser;
import com.kt.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@PatchMapping("/{accountId}/password")
	public ResponseEntity<ApiResult<Void>> updatePassword(
		@PathVariable UUID accountId,
		@RequestBody @Valid AccountRequest.UpdatePassword request
	){
		accountService.updatePassword(
			accountId,
			request.currentPassword(),
			request.newPassword()
		);
		return ApiResult.ok(null);
	}

	@DeleteMapping("/retire")
	public ResponseEntity<ApiResult<Void>> deleteAccount(
		@AuthenticationPrincipal DefaultCurrentUser defaultCurrentUser
	){
		UUID accountId = defaultCurrentUser.getId();
		accountService.deleteAccount(accountId);
		return ApiResult.ok(null);
	}

	@GetMapping
	public ResponseEntity<?> getAccountByMember(
		@AuthenticationPrincipal DefaultCurrentUser defaultCurrentUser
	){
		AccountResponse.search account = accountService.getAccountByMember(
			defaultCurrentUser.getId(),
			defaultCurrentUser.getRole()
		);
		return ApiResult.ok(account);
	}
}
