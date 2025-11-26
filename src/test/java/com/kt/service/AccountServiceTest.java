package com.kt.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.kt.constant.Gender;
import com.kt.constant.UserRole;
import com.kt.domain.entity.CourierEntity;
import com.kt.domain.entity.UserEntity;
import com.kt.repository.AccountRepository;
import com.kt.repository.courier.CourierRepository;
import com.kt.repository.user.UserRepository;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

	@Autowired
	AccountService accountService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	CourierRepository courierRepository;

	UserEntity member1;
	UserEntity admin1;
	CourierEntity courier1;
	CourierEntity courier2;

	@BeforeEach
	void setUp() {
		courierRepository.deleteAll();
		userRepository.deleteAll();
		accountRepository.deleteAll();

		member1 = UserEntity.create(
			"회원",
			"aaa",
			"1234",
			UserRole.MEMBER,
			Gender.MALE,
			LocalDate.of(2000, 1, 1),
			"111111"
		);
		admin1 = UserEntity.create(
			"관리자",
			"aaa",
			"1234",
			UserRole.ADMIN,
			Gender.MALE,
			LocalDate.of(2000, 1, 1),
			"111111"
		);

		userRepository.save(member1);
		userRepository.save(admin1);

		courier1 = CourierEntity.create(
			"기사1",
			"courier1@test.com",
			"1234",
			Gender.MALE
		);

		courier2 = CourierEntity.create(
			"기사2",
			"courier2@test.com",
			"1234",
			Gender.MALE
		);

		courierRepository.save(courier1);
		courierRepository.save(courier2);
	}

	@Test
	void 회원_조회_성공() {

		// when
		Page<?> foundMembers = accountService.searchAccounts(
			Pageable.ofSize(10),
			"회원",
			UserRole.MEMBER,
			null
		);

		// then
		assertThat(foundMembers).isNotNull();
		assertThat(foundMembers.getContent()).hasSize(1);
	}

	@Test
	void 관리자_조회_성공() {

		// when
		Page<?> foundAdmins = accountService.searchAccounts(
			Pageable.ofSize(10),
			"관리자",
			UserRole.ADMIN,
			null
		);

		// then
		assertThat(foundAdmins).isNotNull();
		assertThat(foundAdmins.getContent()).hasSize(1);
	}

	@Test
	void 배송기사_조회_성공() {

		// when
		Page<?> foundCouriers = accountService.searchAccounts(
			Pageable.ofSize(10),
			"기사",
			UserRole.COURIER,
			null
		);

		// then
		assertThat(foundCouriers).isNotNull();
		assertThat(foundCouriers.getContent()).hasSize(2);
	}

}