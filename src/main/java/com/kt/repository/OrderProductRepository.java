package com.kt.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kt.constant.message.ErrorCode;
import com.kt.domain.entity.OrderProductEntity;
import com.kt.exception.BaseException;

@Repository
public interface OrderProductRepository
	extends JpaRepository<OrderProductEntity, UUID>, OrderProductRepositoryCustom {

	List<OrderProductEntity> findAllByOrder_Id(UUID orderId);

	default OrderProductEntity findByIdOrThrow(UUID orderProductId) {
		return findById(orderProductId).orElseThrow(() -> new BaseException(ErrorCode.ORDER_PRODUCT_NOT_FOUND));
	}

	List<OrderProductEntity> findAllByProduct_Id(UUID productId);
}
