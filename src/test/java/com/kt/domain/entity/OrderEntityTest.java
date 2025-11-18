package com.kt.domain.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.kt.domain.constant.Gender;
import com.kt.domain.constant.UserRole;

@ActiveProfiles("test")
class OrderEntityTest {

	UserEntity testUser;

	@BeforeEach
	void setUp() throws Exception {
		testUser = UserEntity.create(
			"주문자테스터1",
			"wjd123@naver.com",
			"1234",
			UserRole.MEMBER,
			Gender.MALE,
			LocalDate.now(),
			"010-1234-5678"
		)	;
	}

	@Test
	void 객체생성_성공() {
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
			testUser
		);
		assertThat(order.getReceiverVO()).isEqualTo(receiver);
		assertThat(order.getOrderBy()).isEqualTo(testUser);
	}
}