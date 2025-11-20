package com.kt.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kt.constant.message.ErrorCode;
import com.kt.domain.entity.OrderProductEntity;
import com.kt.exception.BaseException;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProductEntity, UUID> {
	default OrderProductEntity findByIdOrThrow(UUID orderProductId) {
		return findById(orderProductId).orElseThrow(() -> new BaseException(ErrorCode.REVIEW_NOT_FOUND));
	}
}
