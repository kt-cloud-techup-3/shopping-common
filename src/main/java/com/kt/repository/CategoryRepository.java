package com.kt.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kt.domain.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
}
