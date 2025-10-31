package com.kt.dto;

import java.time.LocalDate;

import com.kt.domain.Gender;

// loginId, password, name, birthday(YYYY-mm-dd)
public record UserCreateRequest(
	String loginId,
	String password,
	String name,
	String email,
	String mobile,
	Gender gender,
	LocalDate birthday
) {
}
