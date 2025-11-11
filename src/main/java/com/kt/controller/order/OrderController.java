package com.kt.controller.order;

import com.kt.common.ApiResult;

import com.kt.dto.order.OrderRequest;
import com.kt.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ApiResult<Void> create(
		@RequestBody @Valid OrderRequest.Create request
	) {
		orderService.create(
			request.userId(),
			request.productId(),
			request.receiverName(),
			request.receiverAddress(),
			request.receiverMobile(),
			request.quantity()
		);
		return ApiResult.ok();
	}
}
