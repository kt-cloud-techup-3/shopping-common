package com.kt.repository;

import java.util.List;
import java.util.UUID;

import com.kt.dto.response.OrderProductResponse;

public interface OrderProductRepositoryCustom {
	List<OrderProductResponse.SearchReviewable> getReviewableOrderProductsByUserId(UUID userId);
}
