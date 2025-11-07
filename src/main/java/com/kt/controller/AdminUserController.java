package com.kt.controller;

import com.kt.common.ApiResult;

import com.kt.common.Paging;
import com.kt.common.SwaggerAssistance;
import com.kt.dto.user.UserResponse;
import com.kt.dto.user.UserUpdateRequest;
import com.kt.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저 관리", description = "유저 관리 API")
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@RestController
public class AdminUserController extends SwaggerAssistance {
	private final UserService userService;

	@Operation(
		parameters = {
			@Parameter(name = "keyword", description = "검색 키워드(이름)"),
			@Parameter(name = "page", description = "페이지 번호", example = "1"),
			@Parameter(name = "size", description = "페이지 크기", example = "10")
		}
	)
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Page<UserResponse.Search>> search(
		@RequestParam(required = false) String keyword,
		@Parameter(hidden = true) Paging paging
	) {
		var search = userService.search(paging.toPageable(), keyword)
			.map(user -> new UserResponse.Search(
				user.getId(),
				user.getName(),
				user.getCreatedAt()
			));

		return ApiResult.ok(search);

	}

	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<UserResponse.Detail> detail(@PathVariable Long id) {
		var user = userService.detail(id);

		return ApiResult.ok(new UserResponse.Detail(
			user.getId(),
			user.getName(),
			user.getEmail()
		));
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResult<Void> update(@PathVariable Long id, @RequestBody @Valid UserUpdateRequest request) {
		userService.update(id, request.name(), request.email(), request.mobile());

		return ApiResult.ok();
	}
}
