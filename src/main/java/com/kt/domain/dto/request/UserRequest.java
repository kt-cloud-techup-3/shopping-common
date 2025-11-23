package com.kt.domain.dto.request;

import java.time.LocalDate;

import com.kt.constant.Gender;

import jakarta.validation.constraints.NotBlank;

public class UserRequest {

	public record UpdatePassword(
		@NotBlank(message = "비밀번호는 필수항목입니다.")
		String currentPassword,
		@NotBlank(message = "변경할 비밀번호는 반드시 기재되어야합니다.")
		String newPassword
	){ }

	public record UpdateDetails(
		String name,
		String mobile,
		LocalDate birth,
		Gender gender
	){}
}
