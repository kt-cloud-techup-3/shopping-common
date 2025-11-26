package com.kt.repository.orderproduct;

import java.util.List;
import java.util.UUID;

import com.kt.domain.dto.response.OrderProductResponse;

public interface OrderProductRepositoryCustom {
	List<OrderProductResponse.SearchReviewable> getReviewableOrderProductsByUserId(UUID userId);
}
