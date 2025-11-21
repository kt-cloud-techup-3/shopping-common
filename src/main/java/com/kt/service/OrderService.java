package com.kt.service;

import java.util.List;

import com.kt.domain.dto.request.OrderRequest;

public interface OrderService {
	void createOrder(String email, List<OrderRequest.Item> items);
}
