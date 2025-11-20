package com.kt.domain.dto.response;

import java.util.UUID;

import com.kt.domain.entity.ProductEntity;
import com.querydsl.core.annotations.QueryProjection;

public class ProductResponse {
	public record Search(
		UUID id,
		String name,
		Long price
	) {
		@QueryProjection
		public Search {
		}
	}

	public record Detail(
		UUID id,
		String name,
		Long price,
		Long stock
	) {

		public static Detail from(ProductEntity product) {
			return new Detail(
				product.getId(),
				product.getName(),
				product.getPrice(),
				product.getStock()
			);
		}
	}
}
