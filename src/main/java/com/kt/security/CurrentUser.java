package com.kt.security;

import java.util.UUID;

public interface CurrentUser {
	UUID getId();

	String getLoginId();
}
