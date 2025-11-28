package com.kt.domain.dto.request;

import com.kt.constant.CourierWorkStatus;
import com.kt.constant.UserRole;

public record AccountSearchRequestVO(
	String keyword,
	UserRole role,
	CourierWorkStatus workStatus
) {}