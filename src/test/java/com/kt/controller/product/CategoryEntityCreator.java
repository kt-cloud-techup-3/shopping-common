package com.kt.controller.product;

import java.util.UUID;

import com.kt.domain.entity.CategoryEntity;

public class CategoryEntityCreator {
	public static CategoryEntity createCategory() {
		return CategoryEntity.create(
			"category" + UUID.randomUUID(),
			null
		);
	}
}
