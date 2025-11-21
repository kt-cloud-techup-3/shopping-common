package com.kt.service;

import java.util.UUID;

import com.kt.dto.response.ReviewResponse;

public interface ReviewService {
	void create(UUID orderProductId, String content);

	void update(UUID reviewId, String content);

	void delete(UUID reviewId);

	ReviewResponse.Search getReview(UUID orderProductId);
}