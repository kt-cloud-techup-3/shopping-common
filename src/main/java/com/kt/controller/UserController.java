package com.kt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kt.dto.UserCreateRequest;
import com.kt.service.UserService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	// userservice를 di받아야함
	// di받는 방식이 생성자주입 씀 -> 재할당을 금지함

	private final UserService userService;

	@PostMapping("/users")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiResponses(value = {
		// 성공
		@ApiResponse(responseCode = "201", description = "Created"),
		// 클라이언트 오류
		@ApiResponse(responseCode = "400", description = "Bad Request"),
		// 서버 오류
		@ApiResponse(responseCode = "500", description = "Internal Server Error")
	})
	// loginId, password, name, birthday
	// json형태의 body에 담겨서 post요청으로 /users로 들어오면
	// @RequestBody를보고 jacksonObjectMapper가 동작해서 json을 읽어서 dto로 변환
	public void create(@Valid @RequestBody UserCreateRequest request) {
		userService.create(request);
	}
}
