package com.kt.controller.product;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.Paging;
import com.kt.common.api.ApiResult;
import com.kt.constant.searchtype.ProductSearchType;
import com.kt.domain.dto.request.AdminProductRequest;
import com.kt.domain.dto.response.ProductResponse;
import com.kt.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

	private final ProductService productService;

	// TODO: 상품 목록 조회
	// TODO: 상품 상세 조회
	// TODO: 상품 품절 토글
	// TODO: 상품 활성화
	// TODO: 상품 비활성화
	// TODO: 상품 수정
	// TODO: 상품 삭제
	// TODO: 상품 다중 품절
	// TODO: 상품 생성

	@PostMapping
	public ResponseEntity<?> create(
		@RequestBody AdminProductRequest.Create request
	) {
		productService.create(
			request.name(),
			request.price(),
			request.stock(),
			request.categoryId()
		);
		return ApiResult.ok(null);
	}

	@PostMapping("/sold-out")
	public ResponseEntity<?> soldOutProducts(
		@RequestBody AdminProductRequest.SoldOut request
	) {
		productService.soldOutProducts(request.productIds());
		return ApiResult.ok(null);
	}

	@GetMapping
	public ResponseEntity<?> search(
		@ModelAttribute Paging paging,
		@RequestParam(required = false) String keyword,
		@RequestParam(required = false) ProductSearchType type
	) {
		Page<ProductResponse.Search> search = productService.search(paging.toPageable(), keyword, type);
		return ApiResult.ok(search);
	}

}
