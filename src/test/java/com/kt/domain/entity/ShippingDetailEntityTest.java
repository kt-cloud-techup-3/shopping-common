package com.kt.domain.entity;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.kt.constant.Gender;
import com.kt.constant.OrderProductStatus;
import com.kt.constant.ProductStatus;
import com.kt.constant.UserRole;

class ShippingDetailEntityTest {

	OrderProductEntity testOrderProduct;
	CourierEntity testCourier;

	@BeforeEach
	void setUp() throws Exception {
		UserEntity user = UserEntity.create(
			"주문자테스터1",
			"wjd123@naver.com",
			"1234",
			UserRole.MEMBER,
			Gender.MALE,
			LocalDate.now(),
			"010-1234-5678"
		)	;

		ReceiverVO receiver = new ReceiverVO(
			"수신자테스터1",
			"010-1234-5678",
			"강원도",
			"원주시",
			"행구로",
			"주소설명"
		);

		OrderEntity order = OrderEntity.create(
			receiver,
			user
		);

		ProductEntity product= ProductEntity.create(
			"테스트상품명",
			1000L,
			5L,
			ProductStatus.ACTIVATED
		);

		testOrderProduct = new OrderProductEntity(
			5L,
			5000L,
			OrderProductStatus.CREATED,
			order,
			product
		);

		testCourier = CourierEntity.create(
			"테스터1",
			"wjd1234@naver.com",
			"1234",
			Gender.MALE
		);
	}

	@Test
	void 객체생성_성공(){
		ShippingDetailEntity shippingDetailEntity = ShippingDetailEntity.create(
			testCourier,
			testOrderProduct
		);
		assertThat(shippingDetailEntity.getCourier()).isEqualTo(testCourier);
		assertThat(shippingDetailEntity.getOrderProduct()).isEqualTo(testOrderProduct);
	}
}