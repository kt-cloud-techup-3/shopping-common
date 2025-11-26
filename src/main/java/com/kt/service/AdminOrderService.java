package com.kt.service;

import java.util.List;
import java.util.UUID;

import com.kt.constant.OrderStatus;
import com.kt.domain.dto.response.AdminOrderResponse;

public interface AdminOrderService {

	List<AdminOrderResponse.Search> getOrders();

	AdminOrderResponse.Detail getOrder(UUID orderId);

	void changeStatus(UUID orderId, OrderStatus newStatus);
	void cancelOrder(UUID orderId);
}
