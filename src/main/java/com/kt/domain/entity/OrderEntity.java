package com.kt.domain.entity;

import static lombok.AccessLevel.*;

import com.kt.domain.entity.common.BaseEntity;
import com.kt.util.ValidationUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "\"order\"")
@NoArgsConstructor(access = PROTECTED)
public class OrderEntity extends BaseEntity {

	@Embedded
	@Column(nullable = false)
	private ReceiverVO receiverVO;

	@ManyToOne
	@JoinColumn(name = "order_by", nullable = false)
	private UserEntity orderBy;

	protected OrderEntity(
		ReceiverVO receiverVO,
		UserEntity orderBy
	) {
		this.receiverVO = receiverVO;
		this.orderBy = orderBy;
	}

	public static OrderEntity create(
		final ReceiverVO receiverVO,
		final UserEntity orderBy
	) {
		return new OrderEntity(
			receiverVO,
			orderBy
		);
	}
}
