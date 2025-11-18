package com.kt.domain.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductStatus {
	ACTIVATED("판매중"),
	SOLD_OUT("품절"),
	IN_ACTIVATED("판매중지"),
	DELETED("삭제");

	private final String description;
}
