package com.kt.domain.entity;

import com.kt.domain.constant.OrderProductStatus;
import com.kt.domain.entity.common.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
@Entity(name = "order_product")
public class OrderProductEntity extends BaseEntity {
	private Long quantity;
	private Long unitPrice;
	private OrderProductStatus status;

	@ManyToOne
	@JoinColumn(name = "order_id", nullable = false)
	private OrderEntity order;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private ProductEntity product;

}
