package com.kt.domain.dto.request;

public class ProductRequest {
	public record Create(
		String name,
		Long price,
		Long stock
	) {
	}
}
