package com.kt.dto;

import java.util.List;
import java.util.UUID;

import com.kt.domain.entity.CategoryEntity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryResponse {

	public record getAll(
		UUID id,
		String name,
		List<getAll> children
	) {
		public static getAll of(CategoryEntity category) {
			return new getAll(
				category.getId(),
				category.getName(),
				category.getChildren().stream()
					.map(getAll::of)
					.toList()
			);
		}
	}
}
