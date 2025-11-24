package com.kt.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kt.domain.dto.response.ReviewResponse;
import com.kt.domain.entity.OrderProductEntity;
import com.kt.domain.entity.ReviewEntity;
import com.kt.repository.OrderProductRepository;
import com.kt.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;
	private final OrderProductRepository orderProductRepository;

	@Override
	public void create(UUID orderProductId, String content) {
		ReviewEntity review = ReviewEntity.create(content);
		OrderProductEntity orderProduct = orderProductRepository.findByIdOrThrow(orderProductId);
		review.mapToOrderProduct(orderProduct);
		reviewRepository.save(review);
	}

	@Override
	public void update(UUID reviewId, String content) {
		ReviewEntity review = reviewRepository.findByIdOrThrow(reviewId);
		review.update(content);
	}

	@Override
	public void delete(UUID reviewId) {
		ReviewEntity review = reviewRepository.findByIdOrThrow(reviewId);
		review.delete();
	}

	@Override
	public ReviewResponse.Search getReview(UUID orderProductId) {
		ReviewEntity reviewEntity = reviewRepository.findByOrderProductIdOrThrow(orderProductId);
		return new ReviewResponse.Search(
			reviewEntity.getId(),
			reviewEntity.getContent()
		);
	}

	@Override
	public List<ReviewResponse.Search> getReviewByProductId(UUID productId) {
		List<OrderProductEntity> orderProducts = orderProductRepository.findAllByProductId(productId);

		return orderProducts.stream().map(orderProduct -> {
			ReviewEntity reviewEntity = reviewRepository.findByOrderProductIdOrThrow(orderProduct.getId());
			return new ReviewResponse.Search(
				reviewEntity.getId(),
				reviewEntity.getContent()
			);
		}).toList();
	}
}
