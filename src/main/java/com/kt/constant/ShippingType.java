package com.kt.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ShippingType {
	DELIVERING("배송"),
	RETURNING("반품");

	private final String description;
}
