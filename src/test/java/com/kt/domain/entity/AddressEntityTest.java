package com.kt.domain.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.kt.constant.Gender;
import com.kt.constant.UserRole;

@ActiveProfiles("test")
class AddressEntityTest {

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
	void 객체생성_성공(){
		AddressEntity addressEntity = AddressEntity.create(
			"수신자테스터1",
			"010-1234-5678",
			"강원도",
			"원주시",
			"행구로",
			"주소설명",
			testUser
		);
		assertThat(addressEntity.getReceiverName()).isEqualTo("수신자테스터1");
		assertThat(addressEntity.getReceiverMobile()).isEqualTo("010-1234-5678");
		assertThat(addressEntity.getCity()).isEqualTo("강원도");
		assertThat(addressEntity.getDistrict()).isEqualTo("원주시");
		assertThat(addressEntity.getRoadAddress()).isEqualTo("행구로");
		assertThat(addressEntity.getDetail()).isEqualTo("주소설명");
		assertThat(addressEntity.getCreatedBy()).isEqualTo(testUser);
	}
}