package com.kt.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kt.domain.entity.CategoryEntity;
import com.kt.dto.CategoryResponse;
import com.kt.repository.CategoryRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryServiceImplTest {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CategoryRepository categoryRepository;

	@BeforeEach
	void setUp() {

		categoryRepository.deleteAll();

		CategoryEntity parentCategory = categoryRepository.save(
			CategoryEntity.create(
				"부모카테고리명",
				null
			));
		CategoryEntity parentCategory2 = categoryRepository.save(
			CategoryEntity.create(
				"부모카테고리명2",
				null
			));
		CategoryEntity childCategory = categoryRepository.save(
			CategoryEntity.create(
				"자식카데고리명",
				parentCategory
			));
		CategoryEntity childCategory2 = categoryRepository.save(
			CategoryEntity.create(
				"자식카데고리명2",
				parentCategory
			));
	}

	@Test
	void 카테고리_생성() {
		// given
		UUID id = categoryService.create("테스트카테고리", null);

		// when
		CategoryEntity saved = categoryRepository.findById(id).orElse(null);

		// then
		assertThat(saved).isNotNull();
		assertThat(saved.getName()).isEqualTo("테스트카테고리");
		assertThat(saved.getParent()).isNull();
	}

	@Test
	void 카테고리_이름_수정() {
		// given
		CategoryEntity category = categoryRepository.save(
			CategoryEntity.create("기존이름", null)
		);

		// when
		categoryService.update(category.getId(), "수정");

		// then
		CategoryEntity updated = categoryRepository.findById(category.getId()).orElse(null);

		assertThat(updated).isNotNull();
		assertThat(updated.getName()).isEqualTo("수정");
	}

	@Test
	void 카테고리_전체_조회() {
		// when
		List<CategoryResponse.getAll> result = categoryService.getAll();
		// then
		assertThat(result).isNotNull();
		assertThat(result).hasSize(2);

		CategoryResponse.getAll parent = result
			.stream()
			.filter(res -> res.name().equals("부모카테고리명"))
			.findFirst()
			.orElseThrow();

		assertThat(parent).isNotNull();
		assertThat(parent.children()).hasSize(2);
	}

}