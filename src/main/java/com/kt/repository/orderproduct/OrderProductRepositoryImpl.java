package com.kt.repository.orderproduct;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kt.constant.OrderProductStatus;
import com.kt.domain.dto.response.OrderProductResponse;
import com.kt.domain.dto.response.QOrderProductResponse_SearchReviewable;
import com.kt.domain.entity.QOrderEntity;
import com.kt.domain.entity.QOrderProductEntity;
import com.kt.domain.entity.QReviewEntity;
import com.kt.domain.entity.QUserEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderProductRepositoryImpl implements OrderProductRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	private final QUserEntity user = QUserEntity.userEntity;
	private final QOrderEntity order = QOrderEntity.orderEntity;
	private final QOrderProductEntity orderProduct = QOrderProductEntity.orderProductEntity;
	private final QReviewEntity review = QReviewEntity.reviewEntity;

	@Override
	public List<OrderProductResponse.SearchReviewable> getReviewableOrderProductsByUserId(UUID userId) {
		BooleanExpression condition = review
			.orderProduct
			.isNull()
			.and(orderProduct.status.eq(OrderProductStatus.PURCHASE_CONFIRMED));

		return jpaQueryFactory
			.select(new QOrderProductResponse_SearchReviewable(
				orderProduct.id,
				orderProduct.quantity,
				orderProduct.unitPrice,
				orderProduct.status
			))
			.from(user)
			.join(order).on(user.id.eq(order.orderBy.id))
			.join(orderProduct).on(order.id.eq(orderProduct.order.id))
			.leftJoin(review).on(orderProduct.id.eq(review.orderProduct.id))
			.where(condition)
			.fetch();
	}
}