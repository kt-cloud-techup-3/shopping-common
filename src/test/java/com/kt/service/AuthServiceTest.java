package com.kt.service;

import com.kt.constant.Gender;
import com.kt.constant.UserRole;
import com.kt.constant.UserStatus;
import com.kt.constant.redis.RedisKey;
import com.kt.domain.dto.request.LoginRequest;
import com.kt.domain.dto.request.SignupRequest;
import com.kt.domain.entity.UserEntity;
import com.kt.exception.AuthException;
import com.kt.exception.DuplicatedException;
import com.kt.infra.redis.RedisCache;
import com.kt.repository.AccountRepository;
import com.kt.repository.user.UserRepository;

import com.mysema.commons.lang.Pair;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
public class AuthServiceTest {

	final static String SUCCESS_USER_LOGIN = "유저 로그인 성공";
	final static String FAIL_USER_LOGIN_INVALID_PASSWORD = "유저 로그인 실패 비밀번호 틀림";
	final static String FAIL_USER_LOGIN_STATUS_DISABLED = "유저 로그인 실패 비활성화 상태";
	final static String FAIL_USER_LOGIN_STATUS_DELETED = "유저 로그인 실패 삭제 상태";
	final static String FAIL_USER_LOGIN_STATUS_RETIRED = "유저 로그인 실패 탈퇴 상태";
	@Autowired
	AuthServiceImpl authService;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	RedisCache redisCache;
	@Autowired
	RedisTemplate redisTemplate;
	UserEntity user;
	String rawPassword = "1231231!";

	@BeforeEach
	void setUp(TestInfo testInfo) {
		userRepository.deleteAll();
		accountRepository.deleteAll();
		var connection = redisTemplate.getConnectionFactory().getConnection();
		connection.flushAll();
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
		String email = "test@email.com";
		redisCache.set(
			RedisKey.SIGNUP_VERIFIED,
			email,
			true
		);

		SignupRequest.SignupMember signup = new SignupRequest.SignupMember(
			"황테스터",
			email,
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
	// 해야함 인증 정보 null 이거나 false 일때 에러 에러

	@Test
	void 맴버_회원가입_실패_인증정보_없음_시간초과() throws InterruptedException {
		String email = "test@email.com";
		redisCache.set(
			RedisKey.SIGNUP_VERIFIED.key(email),
			true,
			Duration.ofSeconds(2)
		);

		Thread.sleep(2500);

		SignupRequest.SignupMember signup = new SignupRequest.SignupMember(
			"황테스터1",
			email,
			"1231231!",
			Gender.MALE,
			LocalDate.of(2011, 11, 10),
			"010-1234-0001"
		);

		assertThrowsExactly(
			IllegalArgumentException.class, () ->
				authService.memberSignup(signup)
		);
	}

	@Test
	void 맴버_회원가입_실패_인증정보_없음_이메일_키값() {
		String email = "test@email.com";
		String differentEmail = "test_different@email.com";
		redisCache.set(
			RedisKey.SIGNUP_VERIFIED,
			email,
			true
		);

		SignupRequest.SignupMember signup = new SignupRequest.SignupMember(
			"황테스터1",
			differentEmail,
			"1231231!",
			Gender.MALE,
			LocalDate.of(2011, 11, 10),
			"010-1234-0001"
		);

		assertThrowsExactly(
			IllegalArgumentException.class, () ->
				authService.memberSignup(signup)
		);
	}

	@Test
	void 맴버_회원가입_실패_email_중복() {
		String email = "test@email.com";
		redisCache.set(
			RedisKey.SIGNUP_VERIFIED,
			email,
			true
		);
		SignupRequest.SignupMember firstSignup = new SignupRequest.SignupMember(
			"황테스터1",
			email,
			"1231231!",
			Gender.MALE,
			LocalDate.of(2011, 11, 10),
			"010-1234-0001"
		);

		SignupRequest.SignupMember secondSignup = new SignupRequest.SignupMember(
			"황테스터2",
			email,
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

	@Test
	void 유저_회원가입_이메일_인증_성공_redis_저장_데이터_존재() {
		SignupRequest.SignupEmail signupEmail = new SignupRequest.SignupEmail(
			"bjwnstkdbj@naver.com"
		);
		authService.sendAuthCode(signupEmail);
		String value = redisCache.get(
			RedisKey.SIGNUP_CODE.key(signupEmail.email()),
			String.class
		);
		assertNotNull(value);
		log.info("authCode :: {}", value);
	}

	@Test
	void 유저_회원가입_이메일_인증_실패_redis_저장_데이터_미존재() {
		String requestEmail = "bjwnstkdbj@naver.com";
		String differentEmail = "test@email.com";
		SignupRequest.SignupEmail signupEmail = new SignupRequest.SignupEmail(
			requestEmail
		);
		authService.sendAuthCode(signupEmail);
		String value = redisCache.get(
			RedisKey.SIGNUP_CODE.key(differentEmail),
			String.class
		);
		assertNull(value);
		log.info("authCode :: {}", value);
	}

	@Test
	void 유저_회원가입_이메일_검증_redis_저장_데이터_존재() {
		String email = "bjwnstkdbj@naver.com";
		String authCode = "123123";
		redisCache.set(
			RedisKey.SIGNUP_CODE,
			email,
			authCode
		);

		SignupRequest.VerifySignupCode verifyRequest = new SignupRequest.VerifySignupCode(
			email,
			authCode
		);

		authService.verifySignupCode(verifyRequest);

		Boolean isVerify = redisCache.get(
			RedisKey.SIGNUP_VERIFIED.key(email),
			Boolean.class
		);
		assertNotNull(isVerify);
		assertTrue(isVerify);
	}

	@Test
	void 유저_회원가입_이메일_검증_실패_이메일_키값_없음() {
		String originEmail = "bjwnstkdbj@naver.com";
		String differentEmail = "test@email.com";
		String authCode = "123123";
		redisCache.set(RedisKey.SIGNUP_CODE, originEmail, authCode);

		SignupRequest.VerifySignupCode verifyRequest = new SignupRequest.VerifySignupCode(
			differentEmail,
			authCode
		);

		assertThrowsExactly(
			IllegalArgumentException.class, () ->
				authService.verifySignupCode(verifyRequest)
		);
	}

	@Test
	void 유저_회원가입_이메일_검증_실패_인증코드_틀림() {
		String originEmail = "bjwnstkdbj@naver.com";
		String authCode = "123123";
		String differentCode = "999999";
		redisCache.set(RedisKey.SIGNUP_CODE, originEmail, authCode);

		SignupRequest.VerifySignupCode verifyRequest = new SignupRequest.VerifySignupCode(
			originEmail,
			differentCode
		);

		assertThrowsExactly(
			IllegalArgumentException.class, () ->
				authService.verifySignupCode(verifyRequest)
		);
	}

}
