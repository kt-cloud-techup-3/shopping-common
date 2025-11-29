package com.kt.controller.admin;

import static com.kt.common.api.ApiResult.*;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.Paging;
import com.kt.common.api.ApiResult;
import com.kt.domain.dto.request.AccountSearchRequestVO;
import com.kt.domain.dto.request.SignupRequest;
import com.kt.domain.dto.request.UserRequest;
import com.kt.domain.dto.response.UserResponse;
import com.kt.service.AccountService;
import com.kt.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final UserService userService;
	private final AccountService accountService;

	@GetMapping("/admins")
	public ResponseEntity<?> searchAdmins(
		@ModelAttribute Paging paging,
		@ModelAttribute AccountSearchRequestVO accountSearchRequestVO
	) {
		return page(
			accountService.searchAccounts(
				paging.toPageable(),
				accountSearchRequestVO
			)
		);
	}

	@GetMapping("/admins/{adminId}")
	public ResponseEntity<ApiResult<UserResponse.UserDetail>> getAccountDetail(@PathVariable UUID adminId) {
		return wrap(
			userService.getUserDetail(adminId)
		);
	}

}
