package com.kt.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryRequest {
	public record Create(
		@NotBlank
		String title,
		@NotBlank
		UUID parentId
	) {

	}

	public record Update(
		@NotBlank
		String title,
		@NotBlank
		UUID parentId
	) {

	}
}
