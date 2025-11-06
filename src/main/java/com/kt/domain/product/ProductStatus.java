package com.kt.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
	ACTIVE("판매중"),
	SOLD_OUT("품절"),
	INACTIVE("판매중지");

	private final String description;
}
