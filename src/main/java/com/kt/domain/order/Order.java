package com.kt.domain.order;

import com.kt.common.BaseEntity;

import com.kt.domain.orderproduct.OrderProduct;
import com.kt.domain.product.Product;
import com.kt.domain.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
public class Order extends BaseEntity {

	private String receiverName;
	private String receiverAddress;
	private String receiverMobile;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	private LocalDateTime deliveredAt;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "order")
	private List<OrderProduct> orderProducts = new ArrayList<>();

	/*
	* todo..
	* 주문생성
	* 주문상태변경
	* 주문 완료 후 재고 차감
	* 배송 받는 이 주소 변경
	* 주문 취소
	* */

}
