package com.kt.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record OrderRequest(
	@NotNull
	List<Item> items
) {
	public record Item(
		@NotNull
		UUID productId,
		@Min(1)
		Long quantity
	){}
}