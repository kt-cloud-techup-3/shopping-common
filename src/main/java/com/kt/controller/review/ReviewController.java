package com.kt.controller.review;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.api.ApiResult;
import com.kt.domain.dto.request.ReviewRequest;
import com.kt.domain.dto.response.ReviewResponse;
import com.kt.service.ReviewService;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	@GetMapping
	public ResponseEntity<ApiResult<List<ReviewResponse.Search>>> search(
		@RequestParam UUID productId
	){
			List<ReviewResponse.Search> reviewDetail = reviewService.getReviewByProductId(productId);
			return ApiResult.ok(reviewDetail);
	}
}
