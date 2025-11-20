package com.kt.repository;

import java.util.List;
import java.util.UUID;

import com.kt.domain.entity.OrderProductEntity;


public interface UserRepositoryCustom {
	List<OrderProductEntity> getReviewtableOrderProducts(UUID userId);
}