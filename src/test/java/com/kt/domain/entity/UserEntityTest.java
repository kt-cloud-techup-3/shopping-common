package com.kt.domain.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import com.kt.domain.constant.Gender;
import com.kt.domain.constant.UserRole;

@ActiveProfiles("test")
class UserEntityTest {

	@Test
	void 객체생성_성공(){
		UserEntity user = UserEntity.create(
			"주문자테스터1",
			"wjd123@naver.com",
			"1234",
			UserRole.MEMBER,
			Gender.MALE,
			LocalDate.of(1990, 1, 1),
			"010-1234-5678"
		);
		assertThat(user.getName()).isEqualTo("주문자테스터1");
		assertThat(user.getEmail()).isEqualTo("wjd123@naver.com");
		assertThat(user.getPassword()).isEqualTo("1234");
		assertThat(user.getRole()).isEqualTo(UserRole.MEMBER);
		assertThat(user.getGender()).isEqualTo(Gender.MALE);
		assertThat(user.getBirth()).isEqualTo(LocalDate.of(1990, 1, 1));
		assertThat(user.getMobile()).isEqualTo("010-1234-5678");
	}
}