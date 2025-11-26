package com.kt.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
public class AccountRequest {
		public record UpdatePassword(
			@NotBlank(message = "비밀번호는 필수항목입니다.")
			String currentPassword,
			@NotBlank(message = "비밀번호는 필수항목입니다.")
			String newPassword
		){}
}
