package com.kt.repository.account;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AccountRepositoryImpl implements AccountRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;



}
