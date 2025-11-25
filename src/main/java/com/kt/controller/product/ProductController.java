package com.kt.controller.product;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.Paging;
import com.kt.common.api.ApiResult;
import com.kt.constant.searchtype.ProductSearchType;
import com.kt.domain.dto.response.ProductResponse;
import com.kt.domain.dto.response.ReviewResponse;
import com.kt.service.ProductService;
import com.kt.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;
	private final ReviewService reviewService;

	@GetMapping
	public String search(
		@ModelAttribute Paging paging,
		@RequestParam(required = false) String keyword,
		@RequestParam(required = false) ProductSearchType type
	) {
		Page<ProductResponse.Search> search = productService.search(paging.toPageable(), keyword, type);
		return "hihi";
	}

	@GetMapping("/{productId}")
	public ResponseEntity<?> detail(
		@RequestParam UUID productId
	) {
		ProductResponse.Detail detail = productService.detail(productId);
		return ApiResult.ok(detail);
	}

	@GetMapping("/{productId}/reviews")
	public ResponseEntity<?> productReviews(
		@RequestParam UUID productId
	) {
		List<ReviewResponse.Search> reviews = reviewService.getReviewByProductId(productId);
		return ApiResult.ok(reviews);
	}
}
