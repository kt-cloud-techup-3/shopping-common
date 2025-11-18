package com.kt.domain.entity;

import static lombok.AccessLevel.*;

import com.kt.domain.constant.message.ErrorCode;
import com.kt.domain.entity.common.BaseEntity;
import com.kt.exception.FieldValidationException;
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
	@Column(nullable = false)
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
		ValidationUtil.validateNotBlank(receiverVO.getName(), "receiver.name");
		ValidationUtil.validateNotBlank(receiverVO.getMobile(), "receiver.mobile");
		ValidationUtil.validateNotBlank(receiverVO.getCity(), "receiver.city");
		ValidationUtil.validateNotBlank(receiverVO.getDistrict(), "receiver.district");
		ValidationUtil.validateNotBlank(receiverVO.getRoad_address(), "receiver.road_address");
		ValidationUtil.validateNotBlank(receiverVO.getDetail(), "receiver.detail");
		if ( orderBy == null ) throw new FieldValidationException(ErrorCode.ORDER_NOT_FOUND);
		return new OrderEntity(
			receiverVO,
			orderBy
		);
	}
}
