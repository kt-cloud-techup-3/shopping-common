package com.kt.dto.order;

import com.kt.domain.order.OrderStatus;
import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public interface OrderResponse {
	record Search(
		Long id,
		String receiverName,
		String productName,
		Long quantity,
		Long totalPrice,
		OrderStatus status,
		LocalDateTime createdAt
	) {
		@QueryProjection
		public Search {
		}
	}
}
