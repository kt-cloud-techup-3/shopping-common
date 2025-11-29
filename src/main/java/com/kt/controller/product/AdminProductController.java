package com.kt.controller.product;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.Paging;
import com.kt.common.api.ApiResult;
import com.kt.constant.searchtype.ProductSearchType;
import com.kt.domain.dto.request.AdminProductRequest;
import com.kt.domain.dto.response.ProductResponse;
import com.kt.security.CurrentUser;
import com.kt.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

	private final ProductService productService;

	// TODO: 상품 삭제

	@PostMapping
	public ResponseEntity<?> create(
		@RequestBody @Valid AdminProductRequest.Create request
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
		@RequestBody @Valid AdminProductRequest.SoldOut request
	) {
		productService.soldOutProducts(request.productIds());
		return ApiResult.ok(null);
	}

	@GetMapping
	public ResponseEntity<?> search(
		@AuthenticationPrincipal CurrentUser user,
		@ModelAttribute Paging paging,
		@RequestParam(required = false) String keyword,
		@RequestParam(required = false) ProductSearchType type
	) {
		Page<ProductResponse.Search> search = productService.search(
			user.getRole(),
			paging.toPageable(),
			keyword,
			type
		);
		return ApiResult.ok(search);
	}

	@GetMapping("/{productId}")
	public ResponseEntity<?> detail(
		@AuthenticationPrincipal CurrentUser user,
		@PathVariable UUID productId
	) {
		ProductResponse.Detail detail = productService.detail(user.getRole(), productId);
		return ApiResult.ok(detail);
	}

	@PutMapping("/{productId}/toggle-sold-out")
	public ResponseEntity<?> toggleActive(
		@PathVariable UUID productId
	) {
		productService.toggleActive(productId);
		return ApiResult.ok(null);
	}

	@PutMapping("/{productId}/activate")
	public ResponseEntity<?> activate(
		@PathVariable UUID productId
	) {
		productService.activate(productId);
		return ApiResult.ok(null);
	}

	@PutMapping("/{productId}/in-activate")
	public ResponseEntity<?> inActivate(
		@PathVariable UUID productId
	) {
		productService.inActivate(productId);
		return ApiResult.ok(null);
	}

	@PutMapping("/{productId}")
	public ResponseEntity<?> update(
		@PathVariable UUID productId,
		@RequestBody @Valid AdminProductRequest.Update request
	) {
		productService.update(
			productId,
			request.name(),
			request.price(),
			request.stock(),
			request.categoryId()
		);
		return ApiResult.ok(null);
	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<?> delete(
		@PathVariable UUID productId
	) {
		productService.delete(productId);
		return ApiResult.ok(null);
	}

}
