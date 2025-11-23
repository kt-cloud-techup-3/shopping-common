package com.kt.domain.entity;

import static lombok.AccessLevel.*;

import com.kt.constant.ProductStatus;
import com.kt.domain.entity.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	@Enumerated(EnumType.STRING)
	private ProductStatus status;

	protected ProductEntity(
		String name,
		Long price,
		Long stock,
		ProductStatus status
	) {
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.status = status;
	}

	public static ProductEntity create(
		final String name,
		final Long price,
		final Long stock
	) {
		return new ProductEntity(
			name,
			price,
			stock,
			ProductStatus.ACTIVATED
		);
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

	public void update(String name, Long price, Long stock) {
		this.name = name;
		this.price = price;
		this.stock = stock;
	}

	public void delete() {
		this.status = ProductStatus.DELETED;
	}

	public void activate() {
		this.status = ProductStatus.ACTIVATED;
	}

	public void deactivate() {
		this.status = ProductStatus.IN_ACTIVATED;
	}

}
