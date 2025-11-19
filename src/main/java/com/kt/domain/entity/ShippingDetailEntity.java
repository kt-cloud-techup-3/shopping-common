package com.kt.domain.entity;

import java.time.Instant;

import com.kt.domain.constant.ShippingStatus;
import com.kt.domain.entity.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "shipping_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingDetailEntity extends BaseEntity {

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ShippingStatus shippingStatus;

	@Column(nullable = false)
	private Instant arrivedAt;

	@ManyToOne
	@JoinColumn(name = "courier_id", nullable = false)
	private CourierEntity courierEntity;

	@ManyToOne
	@JoinColumn(name = "order_product_id", nullable = false)
	private OrderProductEntity orderProduct;

	protected ShippingDetailEntity(
		Instant arrivedAt,
		CourierEntity courier,
		OrderProductEntity orderProduct
	) {
		this.shippingStatus = ShippingStatus.PENDING_SHIPMENT;
		this.arrivedAt = arrivedAt;
		this.courierEntity = courier;
		this.orderProduct = orderProduct;
	}

	public static ShippingDetailEntity create(
		final Instant arrivedAt,
		final CourierEntity courier,
		final OrderProductEntity orderProduct
	) {
		return new ShippingDetailEntity(arrivedAt, courier, orderProduct);
	}
}
