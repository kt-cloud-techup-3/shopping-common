package com.kt.domain.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.kt.constant.Gender;
import com.kt.constant.ProductStatus;
import com.kt.constant.UserRole;

@ActiveProfiles("test")
class CartEntityTest {

	UserEntity testUser;
	ProductEntity testProduct;
	@BeforeEach
	void setUp() throws Exception {
		testUser = UserEntity.create(
			"주문자테스터1",
			"wjd123@naver.com",
			"1234",
			UserRole.MEMBER,
			Gender.MALE,
			LocalDate.of(1990, 1, 1),
			"010-1234-5678"
		);
		testProduct = ProductEntity.create(
			"테스트상품명",
			1000L,
			5L,
			ProductStatus.ACTIVATED
		);
	}

	@Test
	void 객체생성_성공(){
		CartEntity cart = CartEntity.create(
			5L,
			testUser,
			testProduct
		);
		assertThat(cart.getUser()).isEqualTo(testUser);
		assertThat(cart.getProduct()).isEqualTo(testProduct);
		assertThat(cart.getQuantity()).isEqualTo(5L);
	}
}