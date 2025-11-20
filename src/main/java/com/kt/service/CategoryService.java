package com.kt.service;

import java.util.List;
import java.util.UUID;

import com.kt.dto.CategoryResponse;

public interface CategoryService {
	UUID create(String name, UUID parentId);

	void update(UUID id, String name);

	List<CategoryResponse.CategoryTreeItem> getAll();
}
