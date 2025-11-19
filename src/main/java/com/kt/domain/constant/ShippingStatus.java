package com.kt.domain.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ShippingStatus {
	PENDING_SHIPMENT("배송 대기 중"),
	SHIPPING("배송중"),
	DELIVERED("배송 완료");

	private final String description;
}
