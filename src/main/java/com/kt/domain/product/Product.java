package com.kt.domain.product;

import com.kt.common.BaseEntity;

import com.kt.domain.orderproduct.OrderProduct;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Product extends BaseEntity {
	private String name;
	private Long price;
	private Long stock;
	@Enumerated(EnumType.STRING)
	private ProductStatus status = ProductStatus.ACTIVATED;

	@OneToMany(mappedBy = "product")
	private List<OrderProduct> orderProducts = new ArrayList<>();

	public Product(String name, Long price, Long stock) {
		this.name = name;
		this.price = price;
		this.stock = stock;
	}

	public void update(String name, Long price, Long stock) {
		this.name = name;
		this.price = price;
		this.stock = stock;
	}

	public void soldOut() {
		this.status = ProductStatus.SOLD_OUT;
	}

	public void inActivate() {
		this.status = ProductStatus.IN_ACTIVATED;
	}

	public void activate() {
		this.status = ProductStatus.ACTIVATED;
	}

	public void delete() {
		// 논리삭제
		this.status = ProductStatus.DELETED;
	}

	public void decreaseStock(Long quantity) {
		this.stock -= quantity;
	}

	public void increaseStock(Long quantity) {
		this.stock += quantity;
	}
}
