package com.kt.controller.orderproduct;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.api.ApiResult;
import com.kt.domain.dto.request.ReviewRequest;
import com.kt.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static com.kt.common.api.ApiResult.empty;

@RestController
@RequestMapping("/api/orderproducts")
@RequiredArgsConstructor
public class OrderProductController {
	private final ReviewService reviewService;

	@PostMapping("/{orderProductId}/reviews")
	public ResponseEntity<ApiResult<Void>> create(
		@PathVariable UUID orderProductId,
		@RequestBody @Valid ReviewRequest.Create request
	){
		reviewService.create(orderProductId, request.content());
		return empty();
	}
}
