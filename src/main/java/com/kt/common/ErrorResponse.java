package com.kt.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class ErrorResponse {
	private HttpStatus status;
	private String message;

	public static ResponseEntity<ErrorData> error(HttpStatus status, String message) {
		return ResponseEntity.status(status).body(ErrorData.of(status.series().name(), message));
	}

	@Getter
	@AllArgsConstructor
	public static class ErrorData {
		private String code;
		private String message;

		public static ErrorData of(String code, String message) {
			return new ErrorData(code, message);
		}
	}
}
