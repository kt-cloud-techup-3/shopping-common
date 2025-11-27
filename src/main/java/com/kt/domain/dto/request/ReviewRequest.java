package com.kt.domain.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewRequest {
	public record Create(
		@NotNull(message = "주문상품번호는 필수 항목입니다.")
		UUID orderProductId,
		@NotBlank(message = "리뷰내용은 필수 항목입니다.")
		String content
	){}
}
