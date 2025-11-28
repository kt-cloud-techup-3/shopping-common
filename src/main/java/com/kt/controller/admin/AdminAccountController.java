package com.kt.controller.admin;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.Paging;
import com.kt.domain.dto.request.AccountSearchRequestVO;
import com.kt.service.AccountService;
import com.kt.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminAccountController {
	private final UserService userService;
	private final AccountService accountService;

	@GetMapping("/accounts")
	public ResponseEntity<?> searchAccounts(
		@ModelAttribute Paging paging,
		@ModelAttribute AccountSearchRequestVO accountSearchRequestVO

	) {
		return ResponseEntity.ok(
			accountService.searchAccounts(
				paging.toPageable(),
				accountSearchRequestVO.keyword(),
				accountSearchRequestVO.role(),
				accountSearchRequestVO.workStatus()
			)
		);
	}

	@GetMapping("/users/{userId}")
	public ResponseEntity<?> getAccountDetail(@PathVariable UUID userId) {
		return ResponseEntity.ok(
			userService.getUserDetail(userId)
		);
	}

	@PatchMapping("/users/{userId}/enabled")
	public ResponseEntity<?> enableAccount(@PathVariable UUID userId) {
		userService.enableUser(userId);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/users/{userId}/disabled")
	public ResponseEntity<?> disableAccount(@PathVariable UUID userId) {
		userService.disableUser(userId);
		return ResponseEntity.ok().build();
	}

	@PatchMapping("/users/{userId}/removed")
	public ResponseEntity<?> deleteAccount(@PathVariable UUID userId) {
		userService.deleteUser(userId);
		return ResponseEntity.ok().build();
	}


	}


