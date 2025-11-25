package com.kt.common.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kt.constant.message.ResultCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResult<T> {

	private final String code;
	private final String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final T data;

	public static <T> ResponseEntity<ApiResult<T>> ok(T data) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(
				new ApiResult<>(
					ResultCode.SUCCESS.getCode(),
					ResultCode.SUCCESS.getMessage(),
					data
				)
			);
	}
}
