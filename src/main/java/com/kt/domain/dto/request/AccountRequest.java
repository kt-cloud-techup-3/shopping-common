package com.kt.domain.dto.request;


import com.kt.constant.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AccountRequest {
		public record UpdatePassword(
			@NotBlank(message = "비밀번호는 필수항목입니다.")
			String currentPassword,
			@NotBlank(message = "비밀번호는 필수항목입니다.")
			String newPassword
		){}

	public record UpdateDetails(
		@NotBlank(message = "이름은 필수항목입니다.")
		String name,
		@Email(message = "올바른 이메일 형식이 아닙니다.")
		@NotBlank(message = "이메일은 필수 항목입니다.")
		String email,
		@NotNull(message = "성별은 필수 항목입니다.")
		Gender gender
	){}
}
