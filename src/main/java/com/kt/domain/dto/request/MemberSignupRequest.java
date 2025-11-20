package com.kt.domain.dto.request;

import com.kt.constant.Gender;

import java.time.LocalDate;

public record MemberSignupRequest(
	String name,
	String email,
	String password,
	Gender gender,
	LocalDate birth,
	String mobile
) {
}
