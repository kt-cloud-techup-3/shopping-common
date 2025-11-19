package com.kt.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kt.domain.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
}
