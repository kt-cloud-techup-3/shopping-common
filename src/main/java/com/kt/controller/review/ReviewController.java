package com.kt.controller.review;

import static com.kt.common.api.ApiResult.*;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kt.common.Paging;
import com.kt.common.api.ApiResult;
import com.kt.domain.dto.request.ReviewRequest;
import com.kt.domain.dto.response.ReviewResponse;
import com.kt.service.ReviewService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	@GetMapping
	public ResponseEntity<ApiResult<Page<ReviewResponse.Search>>> search(
		@ModelAttribute Paging paging,
		@RequestParam UUID productId
	){
		return wrap(
			reviewService.getReviewByProductId(
				productId,
				paging.toPageable()
			)
		);
	}

	@PatchMapping("/{reviewId}")
	public ResponseEntity<ApiResult<Void>> update(
		@PathVariable UUID reviewId,
		@RequestBody ReviewRequest.Update request
	){
		reviewService.update(reviewId, request.content());
		return empty();
	}
}
