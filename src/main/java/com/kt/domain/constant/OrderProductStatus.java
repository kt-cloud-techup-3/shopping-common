package com.kt.domain.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderProductStatus {
	CREATED("주문생성"),
	WAITING_PAYMENT("결제대기"),
	FULL_CANCELED("전체주문중단"),
	PARTIAL_CANCELED("부분주문중단"),
	PURCHASE_CANCELED("결제주문중단"),
	SHIPPING_READY("배송대기"),
	SHIPPING("배송중"),
	SHIPPING_COMPLETED("배송완료"),
	WAITING_RETURN("반품대기 "),
	APPROVER_RETURN("반품승인"),
	PICKUP_RETURN("반품수거"),
	SHIPPING_RETURN("반품배송중"),
	ARRIVAL_RETURN("반품도착"),
	CONFIRM_RETURN("반품확정");

	private final String description;
}
