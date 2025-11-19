package com.kt.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentStatus {

	PENDING("결제 대기중"),
	PAID("결제 완료"),
	FAILED("결제 실패"),
	APPLY_REFUND("환불 신청"),
	COMPLETE_REFUND("환불 완료");

	private final String description;
}
