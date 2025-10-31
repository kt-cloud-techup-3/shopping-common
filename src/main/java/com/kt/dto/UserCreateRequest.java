package com.kt.dto;

import java.time.LocalDate;

import com.kt.domain.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

// 강도순서 NotNull < NotEmpty < NotBlank
// @NotNull
// - Null(x), 빈문자열("")(O), 공백("  ")(O)

// @NotEmpty
// - Null(x), 빈문자열("")(X), 공백("  ")(O)

// @NotBlank
// - Null(x), 빈문자열("")(X), 공백("  ")(X)
public record UserCreateRequest(
	@NotBlank
	String loginId,
	@NotBlank
	// 최소 8자, 대문자, 소문자, 숫자, 특수문자 포함
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "비밀번호는 최소 8자이며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
	String password,
	@NotBlank
	String name,
	@NotBlank
	// 이메일 패턴
	@Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "이메일 형식이 올바르지 않습니다.")
	String email,
	@NotBlank
	// 휴대폰 번호 패턴 (예: 010-1234-5678)
	@Pattern(regexp = "^01[0-9]-\\d{3,4}-\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
	String mobile,
	@NotNull
	Gender gender,
	@NotNull
	LocalDate birthday
) {
}
