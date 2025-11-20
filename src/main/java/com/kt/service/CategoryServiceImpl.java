package com.kt.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.constant.message.ErrorCode;
import com.kt.domain.entity.CategoryEntity;
import com.kt.dto.CategoryResponse;
import com.kt.exception.BaseException;
import com.kt.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;

	@Transactional
	@Override
	public void create(String name, UUID parentId) {
		CategoryEntity parentCategoryEntity = (parentId != null) ?
			categoryRepository.findById(parentId).orElseThrow(() -> new BaseException(
				ErrorCode.CATEGORY_NOT_FOUND)) : null;

		CategoryEntity categoryEntity = CategoryEntity.create(name, parentCategoryEntity);
		categoryRepository.save(categoryEntity);
	}

	@Transactional
	@Override
	public void update(UUID id, String name) {
		CategoryEntity categoryEntity = categoryRepository.findById(id).orElseThrow(() -> new BaseException(
			ErrorCode.CATEGORY_NOT_FOUND
		));

		categoryEntity.updateName(name);
	}

	@Override
	public List<CategoryResponse.getAll> getAll() {
		List<CategoryEntity> list = categoryRepository.findAll();

		List<CategoryEntity> rootCategory = list.stream()
			.filter(category -> category.getParent() == null)
			.toList();

		return rootCategory
			.stream().map(CategoryResponse.getAll::of).toList();
	}
}
