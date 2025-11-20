package com.kt.domain.service;

import com.kt.ShoppingApplication;
import com.kt.constant.Gender;
import com.kt.domain.dto.request.MemberRequest;
import com.kt.domain.entity.UserEntity;
import com.kt.repository.UserRepository;

import com.kt.service.AuthServiceImpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(classes = ShoppingApplication.class)
public class AuthServiceTest {

	@Autowired AuthServiceImpl authService;

	@Autowired UserRepository userRepository;


	@Test
	void 맴버_회원가입_성공_테스트() {
		MemberRequest.SignupMember signup = new MemberRequest.SignupMember(
			"황테스터",
			"test@email.com",
			"1231231!",
			Gender.MALE,
			LocalDate.of(2011, 11, 11),
			"010-1234-1234"
		);
		authService.memberSignup(signup);
		UserEntity member = userRepository.findByEmail(signup.email()).orElseGet(
			() -> null
		);
		assertNotNull(member);
	}

	@Test
	void 맴버_회원가입_실패_email_중복() {
		MemberRequest.SignupMember firstSignup = new MemberRequest.SignupMember(
			"황테스터1",
			"test@email.com",
			"1231231!",
			Gender.MALE,
			LocalDate.of(2011, 11, 10),
			"010-1234-0001"
		);

		MemberRequest.SignupMember secondSignup = new MemberRequest.SignupMember(
			"황테스터2",
			"test@email.com",
			"1231231!",
			Gender.MALE,
			LocalDate.of(2011, 11, 11),
			"010-1234-0002"
		);
		authService.memberSignup(firstSignup);

		assertThrowsExactly(
			IllegalArgumentException.class, () ->
				authService.memberSignup(secondSignup)
		);

	}


}
