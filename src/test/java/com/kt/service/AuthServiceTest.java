package com.kt.service;

import com.kt.constant.Gender;
import com.kt.constant.UserRole;
import com.kt.constant.UserStatus;
import com.kt.domain.dto.request.LoginRequest;
import com.kt.domain.dto.request.MemberRequest;
import com.kt.domain.entity.UserEntity;
import com.kt.exception.AuthException;
import com.kt.exception.DuplicatedException;
import com.kt.repository.AccountRepository;
import com.kt.repository.UserRepository;

import com.mysema.commons.lang.Pair;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
public class AuthServiceTest {

	@Autowired AuthServiceImpl authService;

	@Autowired UserRepository userRepository;
	@Autowired AccountRepository accountRepository;
	@Autowired PasswordEncoder passwordEncoder;
	final static String SUCCESS_USER_LOGIN = "유저 로그인 성공";
	final static String FAIL_USER_LOGIN_INVALID_PASSWORD = "유저 로그인 실패 비밀번호 틀림";
	final static String FAIL_USER_LOGIN_STATUS_DISABLED = "유저 로그인 실패 비활성화 상태";
	final static String FAIL_USER_LOGIN_STATUS_DELETED = "유저 로그인 실패 삭제 상태";
	final static String FAIL_USER_LOGIN_STATUS_RETIRED = "유저 로그인 실패 탈퇴 상태";

	UserEntity user;
	String rawPassword = "1231231!";
	@BeforeEach
	void setUp(TestInfo testInfo) {
		userRepository.deleteAll();
		accountRepository.deleteAll();

		switch (testInfo.getDisplayName()) {
			case SUCCESS_USER_LOGIN,
					 FAIL_USER_LOGIN_INVALID_PASSWORD,
					 FAIL_USER_LOGIN_STATUS_DISABLED,
					 FAIL_USER_LOGIN_STATUS_DELETED,
					 FAIL_USER_LOGIN_STATUS_RETIRED -> saveMember();
		}

	}

	void saveMember() {
		user = UserEntity.create(
			"황테스트",
			"test@email.com",
			passwordEncoder.encode(rawPassword),
			UserRole.MEMBER,
			Gender.MALE,
			LocalDate.of(2000, 11, 11),
			"010-1234-5678"
		);
		userRepository.save(user);
		UserEntity foundedUser = userRepository.findById(user.getId()).orElse(null);
		assertNotNull(foundedUser);
	}

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
			DuplicatedException.class, () ->
				authService.memberSignup(secondSignup)
		);

	}

	@Test
	@DisplayName(SUCCESS_USER_LOGIN)
	void 유저_로그인_성공() {
		LoginRequest login = new LoginRequest(
			user.getEmail(),
			rawPassword
		);

		Pair<String, String> result = authService.login(login);

		assertNotNull(result.getFirst());
		assertNotNull(result.getSecond());
		log.info("access token : {}", result.getFirst());
		log.info("refresh token : {}", result.getSecond());

	}

	@Test
	@DisplayName(FAIL_USER_LOGIN_INVALID_PASSWORD)
	void 유저_로그인_실패_비밀번호_틀림() {
		LoginRequest login = new LoginRequest(
			user.getEmail(),
			"invalid_password"
		);

		assertThrowsExactly(
			AuthException.class,
			() -> authService.login(login)
		);
	}

	@Test
	@Transactional
	@DisplayName(FAIL_USER_LOGIN_STATUS_DISABLED)
	void 유저_로그인_실패_비활성화_상태() {
		user.disabled();
		assertEquals(UserStatus.DISABLED, user.getStatus());
		LoginRequest login = new LoginRequest(
			user.getEmail(),
			rawPassword
		);

		assertThrowsExactly(
			AuthException.class,
			() -> authService.login(login)
		);
	}

	@Test
	@Transactional
	@DisplayName(FAIL_USER_LOGIN_STATUS_DELETED)
	void 유저_로그인_실패_삭제_상태() {
		user.delete();
		assertEquals(UserStatus.DELETED, user.getStatus());
		LoginRequest login = new LoginRequest(
			user.getEmail(),
			rawPassword
		);

		assertThrowsExactly(
			AuthException.class,
			() -> authService.login(login)
		);
	}

	@Test
	@Transactional
	@DisplayName(FAIL_USER_LOGIN_STATUS_DELETED)
	void 유저_로그인_실패_탈퇴_상태() {
		user.retired();
		assertEquals(UserStatus.RETIRED, user.getStatus());
		LoginRequest login = new LoginRequest(
			user.getEmail(),
			rawPassword
		);

		assertThrowsExactly(
			AuthException.class,
			() -> authService.login(login)
		);
	}




}
