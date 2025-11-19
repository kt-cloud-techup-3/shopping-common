package com.kt.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ShippingType {
	DELIVERING("상품배송"),
	REFUNDING("반품배송");

	private final String description;
}
