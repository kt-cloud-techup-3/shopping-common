package com.kt.domain.dto.response;

import java.util.UUID;

import com.querydsl.core.annotations.QueryProjection;

public class ReviewResponse {
	public record Search(
		UUID reviewId,
		String content
	) {
		@QueryProjection
		public Search {
		}
	}
}
