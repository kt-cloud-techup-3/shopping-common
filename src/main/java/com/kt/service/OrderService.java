package com.kt.service;

import java.util.UUID;
import java.util.List;

import com.kt.domain.dto.response.OrderResponse;
import com.kt.domain.dto.request.OrderRequest;

public interface OrderService {
	OrderResponse.OrderProducts getOrderProducts(UUID orderId);
	void createOrder(String email, List<OrderRequest.Item> items);
	void cancelOrder(UUID orderId);
	void updateOrder(UUID orderId, OrderRequest.Update orderRequest);
}
