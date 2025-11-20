package com.kt.domain.dto.response;

import java.util.UUID;

import com.querydsl.core.annotations.QueryProjection;

public class ProductResponse {
	public record Search(
		UUID id,
		String name,
		Long price,
		Long stock
	) {
		@QueryProjection
		public Search {
		}
	}
}
