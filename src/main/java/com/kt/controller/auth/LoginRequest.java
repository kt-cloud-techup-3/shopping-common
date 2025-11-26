package com.kt.controller.auth;

public record LoginRequest(
	String email,
	String password
) {

}
