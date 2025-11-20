package com.kt.service;


import java.util.UUID;


import java.util.List;
import com.kt.domain.entity.OrderProductEntity;

public interface UserService{
	List<OrderProductEntity> getReviewableOrderProducts(UUID userId);
}
