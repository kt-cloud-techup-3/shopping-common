package com.kt.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

		@NotNull
		@Min(1)
		Long quantity
	){}

	public record Update(
		@NotBlank String receiverName,
		String receiverMobile,
		@NotBlank String city,
		@NotBlank String district,
		@NotBlank String roadAddress,
		String detail,
		@NotNull List<Item> items
	) {}
}