package com.kt.domain.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductStatus {
	// 상품 상태 뭐뭐 있나요..
	AVAILABLE("Available"),
	DELETED("Deleted")
	;
	private final String description;
}
