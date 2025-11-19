package com.kt.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ShippingStatus {
	PENDING_SHIPMENT("배송대기중"),
	SHIPPING("배송중"),
	DELIVERED("배송완료");

	private final String description;
}
