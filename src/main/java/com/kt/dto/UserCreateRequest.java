package com.kt.dto;

import com.kt.domain.Gender;

import java.time.LocalDate;

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
