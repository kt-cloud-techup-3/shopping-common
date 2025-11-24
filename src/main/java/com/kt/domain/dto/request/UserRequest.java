package com.kt.domain.dto.request;

import java.time.LocalDate;

import com.kt.constant.Gender;

import jakarta.validation.constraints.NotBlank;

public class UserRequest {

	public record UpdatePassword(
		@NotBlank(message = "비밀번호는 필수항목입니다.")
		String currentPassword,
		@NotBlank(message = "비밀번호는 필수항목입니다.")
		String newPassword
	){ }

	public record UpdateDetails(
		String name,
		String mobile,
		LocalDate birth,
		Gender gender
	){}
}
