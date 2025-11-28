package com.kt.domain.dto.request;
import java.time.LocalDate;

import com.kt.constant.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UserRequest {
	public record UpdateDetails(
		@NotBlank(message = "이름은 필수항목입니다.")
		String name,
		@NotBlank(message = "휴대폰 번호는 필수 항목입니다.")
		@Pattern(
			regexp = "^(0\\d{1,2})-(\\d{3,4})-(\\d{4})$",
			message = "올바른 휴대폰 번호 형식이 아닙니다."
		)
		String mobile,
		@NotNull(message = "생일날짜는 필수 항목입니다.")
		LocalDate birth,
		@NotNull(message = "성별은 필수 항목입니다.")
		Gender gender
	){}
}