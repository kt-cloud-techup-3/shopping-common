package com.kt.service;

import java.util.List;
import java.util.UUID;

import com.kt.domain.dto.response.ReviewResponse;

public interface ReviewService {
	void create(UUID orderProductId, String content);

	void update(UUID reviewId, String content);

	void delete(UUID reviewId);

	ReviewResponse.Search getReview(UUID orderProductId);

	List<ReviewResponse.Search> getReviewByProductId(UUID productId);
}