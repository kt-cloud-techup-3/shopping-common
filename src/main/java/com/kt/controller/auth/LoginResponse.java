package com.kt.controller.auth;

public record LoginResponse(
	String accessToken,
	String refreshToken
) {
}
