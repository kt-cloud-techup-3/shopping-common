package com.kt.common;

import java.util.UUID;

import com.kt.constant.UserRole;
import com.kt.security.DefaultCurrentUser;


public class CurrentUserCreator {
	public static DefaultCurrentUser getAdminUserDetails() {
		return new DefaultCurrentUser(
			UUID.randomUUID(),
			"admin@naver.com",
			UserRole.ADMIN
		);
	}
}
