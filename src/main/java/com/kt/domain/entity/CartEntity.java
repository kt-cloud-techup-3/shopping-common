package com.kt.domain.entity;

import com.kt.domain.entity.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "cart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartEntity extends BaseEntity {
	@Column(nullable = false)
	private Long quantity;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity userId;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private ProductEntity productId;

	protected CartEntity(
		Long quantity,
		UserEntity userId,
		ProductEntity productId
	) {
		this.quantity = quantity;
		this.userId = userId;
		this.productId = productId;
	}

	public static CartEntity create(
		final Long quantity,
		final UserEntity userId,
		final ProductEntity productId
	) {
		return new CartEntity(quantity, userId, productId);
	}
}
