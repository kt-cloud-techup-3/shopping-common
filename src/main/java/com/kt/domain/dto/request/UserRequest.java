package com.kt.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UserRequest {
	public record updatePassword(
		@NotBlank(message = "비밀번호는 필수항목입니다.")
		String oldPassword,
		@NotBlank(message = "변경할 비밀번호는 반드시 기재되어야합니다.")
		String newPassword
	){ }
}
