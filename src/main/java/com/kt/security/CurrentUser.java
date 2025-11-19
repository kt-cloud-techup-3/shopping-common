package com.kt.security;

import java.util.UUID;

import com.kt.domain.constant.UserRole;

public interface CurrentUser {
	UUID getId();

	UserRole getRole();
}
