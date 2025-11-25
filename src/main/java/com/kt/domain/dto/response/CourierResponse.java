package com.kt.domain.dto.response;

import java.util.UUID;

import com.kt.constant.CourierWorkStatus;
import com.kt.constant.Gender;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourierResponse {

	public record Search(
		UUID id,
		String name,
		String email,
		Gender gender,
		CourierWorkStatus status
	) {
		@QueryProjection
		public Search {
		}

	}
}
