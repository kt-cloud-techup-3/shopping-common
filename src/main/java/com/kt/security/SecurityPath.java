package com.kt.security;

import java.util.Map;

public final class SecurityPath {

	private SecurityPath() {}

	public static final String[] PUBLIC = {
		"/api/auth/**",
	};

	public static final String[] AUTHENTICATED = {
		"/api/matches/**"
	};

	public static final String[] MEMBER = {
		"api/orders/**"
	};

	public static final String[] ADMIN = {
		"/api/admin/**"
	};

	public static final String[] COURIER = {
		"/api/couriers/**"
	};

	public static final Map<String, String[]> ROLE_PATHS = Map.of(
		"ROLE_MEMBER", MEMBER,
		"ROLE_ADMIN", ADMIN,
		"ROLE_COURIER", COURIER
	);
}