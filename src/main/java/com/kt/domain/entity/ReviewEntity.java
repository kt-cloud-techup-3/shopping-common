package com.kt.domain.entity;

import static lombok.AccessLevel.*;

import org.apache.el.util.Validation;

import com.kt.constant.ReviewStatus;
import com.kt.domain.entity.common.BaseEntity;
import com.kt.util.ValidationUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "review")
@NoArgsConstructor(access = PROTECTED)
public class ReviewEntity extends BaseEntity {
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ReviewStatus status;

	@Column(nullable = false)
	private String content;

	@OneToOne
	@JoinColumn(name = "order_product_id", nullable = false)
	private OrderProductEntity orderProduct;


	protected ReviewEntity(String content, ReviewStatus status) {
		ValidationUtil.validateNotNullAndBlank(content,"내용");
		this.status = status;
		this.content = content;
	}

	public static ReviewEntity create(final String content){
		return new ReviewEntity(content, ReviewStatus.ENABLED);
	}
}
