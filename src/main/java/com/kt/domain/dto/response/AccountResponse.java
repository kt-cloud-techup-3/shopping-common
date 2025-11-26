package com.kt.domain.dto.response;

import java.util.UUID;

import com.kt.constant.Gender;
import com.kt.constant.UserRole;

public class AccountResponse {
	public record search(
		UUID accoundId,
		String name,
		String email,
		UserRole role,
		Gender gender
	){ }
}
