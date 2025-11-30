package com.kt.api.account;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;


import com.kt.common.CourierEntityCreator;
import com.kt.common.TestWithMockMvc;
import com.kt.constant.UserStatus;
import com.kt.domain.entity.AbstractAccountEntity;
import com.kt.domain.entity.CourierEntity;
import com.kt.repository.AccountRepository;
import com.kt.repository.courier.CourierRepository;
import com.kt.security.DefaultCurrentUser;

@DisplayName("계정 탈퇴 - DELETE /api/accounts/retire")
public class AccountDeleteTest extends TestWithMockMvc {
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	CourierRepository courierRepository;
	@Autowired
	MockMvc mockMvc;

	CourierEntity testCourier;
	DefaultCurrentUser courierDetails;

	@BeforeEach
	void setUp() throws Exception {
		testCourier = CourierEntityCreator.createCourierEntity();
		courierRepository.save(testCourier);

		courierDetails = new DefaultCurrentUser(
			testCourier.getId(),
			testCourier.getEmail(),
			testCourier.getRole()
		);
	}

	@Test
	void 배송기사계정탈퇴_성공__200_OK() throws Exception {
		mockMvc.perform(delete("/api/accounts/retire")
			.with(SecurityMockMvcRequestPostProcessors.user(courierDetails))
		).andExpect(status().isOk());

		AbstractAccountEntity savedAccount = accountRepository.findByIdOrThrow(testCourier.getId());
		Assertions.assertEquals(UserStatus.DELETED,savedAccount.getStatus());
	}
}
