package com.kt.controller.admin;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.Paging;
import com.kt.constant.CourierWorkStatus;
import com.kt.constant.UserRole;
import com.kt.service.AccountService;
import com.kt.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminAccountController {
	private final UserService userService;
	private final AccountService accountService;

	@GetMapping
	public ResponseEntity<?> searchAccounts(
		@ModelAttribute Paging paging,
		@RequestParam(required = false) String keyword,
		@RequestParam(required = false) UserRole role,
		@RequestParam(required = false)CourierWorkStatus workStatus

	) {
		return ResponseEntity.ok(
			accountService.searchAccounts(
				paging.toPageable(),
				keyword,
				role,
				workStatus
			)
		);
	}

	@GetMapping("/{accountId}")
	public ResponseEntity<?> getAccountDetail(@PathVariable UUID accountId) {
		return ResponseEntity.ok(
			userService.getUserDetail(accountId) // 일반 사용자, 기사도 포함?
		);
	}

	@PostMapping("/{accountId}/enable")
	public ResponseEntity<?> enableAccount(@PathVariable UUID accountId) {
		userService.enableUser(accountId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{accountId}/disable")
	public ResponseEntity<?> disableAccount(@PathVariable UUID accountId) {
		userService.enableUser(accountId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{accountId}")
	public ResponseEntity<?> deleteAccount(@PathVariable UUID accountId) {
		userService.deleteUser(accountId);
		return ResponseEntity.ok().build();
	}


	}


