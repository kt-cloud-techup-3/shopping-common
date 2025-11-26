package com.kt.repository.review;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kt.constant.message.ErrorCode;
import com.kt.domain.entity.ReviewEntity;
import com.kt.exception.BaseException;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, UUID>, ReviewRepositoryCustom{
	default ReviewEntity findByIdOrThrow(UUID reviewId) {
		return findById(reviewId).orElseThrow(() -> new BaseException(ErrorCode.REVIEW_NOT_FOUND));
	}

	Optional<ReviewEntity> findByOrderProductId(UUID orderProductId);

	default ReviewEntity findByOrderProductIdOrThrow(UUID orderProductId) {
		return findByOrderProductId(orderProductId).orElseThrow(() -> new BaseException(ErrorCode.REVIEW_NOT_FOUND));
	}
}
