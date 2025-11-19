package com.kt.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public void create(UUID orderProductId, String content){
		ReviewEntity reviewEntity = ReviewEntity.create(content);
		OrderProductEntity orderProductEntity = orderProductRepository.findByIdOrThrow(orderProductId);
		reviewEntity.mapToOrderProduct(orderProductEntity);
		reviewRepository.save(reviewEntity);
	}

	@Override
	public void update(UUID reviewId, String content){
		ReviewEntity foundedReview = reviewRepository.findByIdOrThrow(reviewId);
		foundedReview.update(content);
	}

	@Override
	public void delete(UUID reviewId){
		ReviewEntity foundedReview = reviewRepository.findByIdOrThrow(reviewId);
		foundedReview.delete();
	}
}
