package com.kt.controller.order;

import com.kt.common.ApiResult;
import com.kt.common.Paging;
import com.kt.dto.order.OrderResponse;
import com.kt.repository.order.OrderRepository;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {
	private final OrderRepository orderRepository;

	@GetMapping
	public ApiResult<Page<OrderResponse.Search>> search(
		@RequestParam(required = false) String keyword,
		@Parameter(hidden = true) Paging paging
	) {
		return ApiResult.ok(orderRepository.search(keyword, paging.toPageable()));
	}
}
