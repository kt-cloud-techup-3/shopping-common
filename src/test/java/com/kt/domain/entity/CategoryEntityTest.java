package com.kt.domain.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CategoryEntityTest {

	@Test
	void 객체생성_성공(){
		CategoryEntity parentCategory = CategoryEntity.create(
			"부모카테고리명",
			null
		);
		CategoryEntity childCategory = CategoryEntity.create(
				"자식카데고리명",
				parentCategory
		);
		assertThat(childCategory.getParent()).isEqualTo(parentCategory);
		assertThat(childCategory.getName()).isEqualTo("자식카데고리명");
	}
}