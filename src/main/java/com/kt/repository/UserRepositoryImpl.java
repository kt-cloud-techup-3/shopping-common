package com.kt.repository;


import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.kt.constant.OrderProductStatus;
import com.kt.domain.entity.OrderProductEntity;
import com.kt.domain.entity.QOrderEntity;
import com.kt.domain.entity.QOrderProductEntity;
import com.kt.domain.entity.QReviewEntity;
import com.kt.domain.entity.QUserEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;


import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

	private final JPAQueryFactory jpaQueryFactory;

	private final QUserEntity qUser =  QUserEntity.userEntity;
	private final QOrderEntity qOrder = QOrderEntity.orderEntity;
	private final QOrderProductEntity qOrderProduct = QOrderProductEntity.orderProductEntity;
	private final QReviewEntity qReview = QReviewEntity.reviewEntity;

	@Override
	public List<OrderProductEntity> getReviewtableOrderProducts(UUID userId) {
		BooleanExpression condition = qReview
			.orderProduct
			.isNull()
			.and(qOrderProduct.status.eq(OrderProductStatus.PURCHASE_CONFIRMED));

		return jpaQueryFactory
			.select(qOrderProduct)
			.from(qUser)
			.join(qOrder).on(qUser.id.eq(qOrder.orderBy.id))
			.join(qOrderProduct).on(qOrder.id.eq(qOrderProduct.order.id))
			.leftJoin(qReview).on(qOrderProduct.id.eq(qReview.orderProduct.id))
			.where(condition)
			.fetch();
	}
}
