package com.kt.domain.dto.request;

import java.util.List;
import java.util.UUID;

public class AdminProductRequest {
	public record Create(
		String name,
		Long price,
		Long stock,
		UUID categoryId
	) {
	}

	public record SoldOut(
		List<UUID> productIds
	) {
	}
}
