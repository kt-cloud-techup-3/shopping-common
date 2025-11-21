package com.kt.service;


import java.util.UUID;


import java.util.List;
import com.kt.dto.response.OrderProductResponse;

public interface UserService{
	List<OrderProductResponse.SearchReviewable> getReviewableOrderProducts(UUID userId);
}
