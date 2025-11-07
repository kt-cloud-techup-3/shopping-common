package com.kt.dto.user;

import com.kt.domain.user.Gender;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class UserRequest {
	@Schema(name = "UserRequest.Create")
	public record Create(
		@NotBlank
		String loginId,
		@NotBlank
		@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^])[A-Za-z\\d!@#$%^]{8,}$")
		String password,
		@NotBlank
		String name,
		@NotBlank
		@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
		String email,
		@NotBlank
		@Pattern(regexp = "^(0\\d{1,2})-(\\d{3,4})-(\\d{4})$")
		String mobile,
		@NotNull
		Gender gender,
		@NotNull
		LocalDate birthday
	) {
	}

	@Schema(name = "UserRequest.Update")
	public record Update(
		@NotBlank
		String name,
		@NotBlank
		@Pattern(regexp = "^(0\\d{1,2})-(\\d{3,4})-(\\d{4})$")
		String mobile,
		@NotBlank
		@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
		String email
	) {
	}
}
