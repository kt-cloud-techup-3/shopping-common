package com.kt.domain.product;

import com.kt.common.BaseEntity;

import com.kt.domain.orderproduct.OrderProduct;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Product extends BaseEntity {

	private String name;
	private Long price;
	private Long stock;

	@Enumerated(EnumType.STRING)
	private ProductStatus productStatus;

	@OneToMany(mappedBy = "product")
	private List<OrderProduct> orderProducts = new ArrayList<>();

	/*
	* todo..
	*
	* 생성
	* 수정
	* 삭제
	* 조회 (리스트, 단일)
	* 상태변경
	* 재고 수량 감소
	* 재고 수량 증가
	*
	* */

}
