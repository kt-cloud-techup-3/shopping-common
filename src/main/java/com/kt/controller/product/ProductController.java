package com.kt.controller.product;

import java.util.List;
import java.util.UUID;

import com.kt.common.api.PageResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.api.ApiResult;
import com.kt.constant.searchtype.ProductSearchType;
import com.kt.domain.dto.response.ProductResponse;
import com.kt.domain.dto.response.ReviewResponse;
import com.kt.security.CurrentUser;
import com.kt.service.ProductService;
import com.kt.service.ReviewService;

import lombok.RequiredArgsConstructor;

import static com.kt.common.api.ApiResult.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;
	private final ReviewService reviewService;

	@GetMapping
	public ResponseEntity<ApiResult<PageResponse<ProductResponse.Search>>> search(
		@AuthenticationPrincipal CurrentUser user,
		@RequestParam(required = false) String keyword,
		@RequestParam(required = false) ProductSearchType type,
		Pageable pageable
	) {
		return page(
			productService.search(
				user.getRole(),
				keyword,
				type,
				pageable
			)
		);
	}

	@GetMapping("/{productId}")
	public ResponseEntity<ApiResult<ProductResponse.Detail>> detail(
		@AuthenticationPrincipal CurrentUser user,
		@PathVariable UUID productId
	) {
		return wrap(
			productService.detail(user.getRole(), productId)
		);
	}

	@GetMapping("/{productId}/reviews")
	public ResponseEntity<ApiResult<List<ReviewResponse.Search>>> productReviews(
		@PathVariable UUID productId
	) {
		return wrap(
			reviewService.getReviewByProductId(productId)
		);
	}
}
