package com.kt.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kt.constant.message.ErrorCode;
import com.kt.domain.entity.ProductEntity;
import com.kt.exception.BaseException;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

	default ProductEntity findByIdOrThrow(UUID productId) {
		return findById(productId).orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));
	}
}
