package com.kt.domain.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.kt.constant.Gender;

@ActiveProfiles("test")
class CourierEntityTest {
	@Test
	void 객체생성_성공(){
		CourierEntity courierEntity = CourierEntity.create(
			"테스터1",
			"wjd1234@naver.com",
			"1234",
			Gender.MALE
		);
		assertThat(courierEntity.getName()).isEqualTo("테스터1");
		assertThat(courierEntity.getEmail()).isEqualTo("wjd1234@naver.com");
		assertThat(courierEntity.getPassword()).isEqualTo("1234");
		assertThat(courierEntity.getGender()).isEqualTo(Gender.MALE);
	}
}