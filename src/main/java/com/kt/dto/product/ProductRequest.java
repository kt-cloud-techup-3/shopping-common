package com.kt.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ProductRequest {
	@Getter
	@AllArgsConstructor
	@Schema(name = "ProductRequest.Create")
	public static class Create {
		@NotBlank
		private String name;
		@NotNull
		private Long price;
		@NotNull
		private Long quantity;
	}

	@Getter
	@AllArgsConstructor
	@Schema(name = "ProductRequest.Update")
	public static class Update {
		@NotBlank
		private String name;
		@NotNull
		private Long price;
		@NotNull
		private Long quantity;
	}
}
