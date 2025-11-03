package com.kt.dto;

import java.time.LocalDate;

// loginId, password, name, birthday(YYYY-mm-dd)
public record UserCreateRequest(
	String loginId,
	String password,
	String name,
	LocalDate birthday
) {
}
