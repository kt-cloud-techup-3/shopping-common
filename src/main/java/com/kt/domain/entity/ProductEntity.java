package com.kt.domain.entity;

import static lombok.AccessLevel.*;

import com.kt.domain.constant.ProductStatus;
import com.kt.domain.entity.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "product")
@NoArgsConstructor(access = PROTECTED)
public class ProductEntity extends BaseEntity {

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Long price;

	@Column(nullable = false)
	private Long stock;

	@Column(nullable = false)
	private ProductStatus status;

	protected ProductEntity(
		final String name,
		final Long price,
		final Long stock,
		final ProductStatus status
	) {
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.status = status;
	}

	public static ProductEntity create(
		final String name,
		final Long price,
		final Long stock,
		final ProductStatus status
	) {
		return new ProductEntity(
			name,
			price,
			stock,
			status
		);
	}
}
