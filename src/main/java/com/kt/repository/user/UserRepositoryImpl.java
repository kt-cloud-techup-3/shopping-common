package com.kt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.kt.domain.dto.response.QUserResponse_Search;
import com.kt.domain.dto.response.UserResponse;
import com.kt.domain.entity.QUserEntity;
import com.kt.repository.user.UserRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	private final QUserEntity user = QUserEntity.userEntity;

	@Override
	public Page<UserResponse.Search> searchUsers(Pageable pageable) {

		BooleanExpression condition = isEnabled();

		var contentQuery = jpaQueryFactory
			.select(new QUserResponse_Search(
				user.id,
				user.name,
				user.email,
				user.role,
				user.gender,
				user.birth,
				user.mobile
			))
			.from(user)
			.where(condition)
			.orderBy(user.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize());

		var countQuery = jpaQueryFactory
			.select(user.count())
			.from(user)
			.where(condition);

		return new PageImpl<>(
			contentQuery.fetch(),
			pageable,
			countQuery.fetchOne()
		);
	}

	private BooleanExpression isEnabled() {
		return null;
	}
}
