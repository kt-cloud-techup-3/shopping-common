package com.kt.dto.response;

import java.util.UUID;


public class ReviewResponse {
	public record Search(
		UUID reviewId,
		String content
	){
	}
}
