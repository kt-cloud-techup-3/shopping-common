package com.kt.service;

import java.util.UUID;

public interface ReviewService {
	void create(UUID orderProductId , String content);

	void update(UUID reviewId, String content);

	void delete(UUID reviewId);
}
