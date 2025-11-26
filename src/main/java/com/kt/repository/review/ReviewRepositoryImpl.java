package com.kt.repository.review;

import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.kt.constant.searchtype.ProductSearchType;
import com.kt.domain.dto.response.QReviewResponse_Search;
import com.kt.domain.dto.response.ReviewResponse;
import com.kt.domain.entity.QCategoryEntity;
import com.kt.domain.entity.QOrderProductEntity;
import com.kt.domain.entity.QProductEntity;
import com.kt.domain.entity.QReviewEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;
	private final QProductEntity product = QProductEntity.productEntity;
	private final QCategoryEntity category = QCategoryEntity.categoryEntity;
	private final QReviewEntity review = QReviewEntity.reviewEntity;
	private final QOrderProductEntity orderProduct = QOrderProductEntity.orderProductEntity;

	@Override
	public Page<ReviewResponse.Search> searchReviews(Pageable pageable, String keyword, ProductSearchType type){

		BooleanBuilder booleanBuilder = new BooleanBuilder();
		booleanBuilder.and(containsKeyword(keyword, type));

		List<ReviewResponse.Search> content = jpaQueryFactory
			.select(new QReviewResponse_Search(
				review.id,
				review.content
			))
			.from(product)
			.join(category).on(category.id.eq(product.category.id))
			.join(orderProduct).on(orderProduct.product.id.eq(product.id))
			.join(review).on(review.orderProduct.id.eq(orderProduct.id))
			.where(booleanBuilder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		int total = jpaQueryFactory.select(product.id)
			.from(product)
			.join(orderProduct).on(orderProduct.product.id.eq(product.id))
			.join(review).on(review.orderProduct.id.eq(orderProduct.id))
			.where(booleanBuilder)
			.fetch().size();

		return new PageImpl<>(content, pageable, total);
	}

	private BooleanExpression containsKeyword(String keyword, ProductSearchType type) {
		if (type == null) return null;
		if (Strings.isBlank(keyword)) return null;
		return (type == ProductSearchType.CATEGORY)?
			category.name.containsIgnoreCase(keyword):
			product.name.containsIgnoreCase(keyword);
	}
}
