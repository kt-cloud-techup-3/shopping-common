package com.kt.constant.message;

import java.text.MessageFormat;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	INVALID_DOMAIN_FIELD(HttpStatus.BAD_REQUEST, "도메인 필드 오류 : {0}"),
	BODY_FIELD_ERROR(HttpStatus.BAD_REQUEST, "바디 필드 오류 : {0}"),
	ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 계정입니다"),
	AUTH_FAILED_LOGIN(HttpStatus.UNAUTHORIZED, "이메일 혹은 비밀번호가 일치하지 않습니다."),
	AUTH_ACCOUNT_DELETED(HttpStatus.FORBIDDEN, "해당 계정은 삭제된 계정입니다"),
	AUTH_ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, "해당 계정은 비활성화된 계정입니다."),
	AUTH_ACCOUNT_RETIRED(HttpStatus.FORBIDDEN, "해당 계정은 탈퇴한 계정입니다."),
	DUPLICATED_EMAIL(HttpStatus.CONFLICT, "해당 이메일로 등록된 계정이 이미 존재합니다."),
	ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문은 존재하지 않습니다."),
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품은 존재하지 않습니다."),
	REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다."),
	PARENT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "부모 카테고리가 존재하지 않습니다."),
	ORDER_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문상품이 존재하지 않습니다."),
	CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리가 존재하지 않습니다."),
	CHILD_CATEGORY_EXISTS(HttpStatus.BAD_REQUEST, "자식 카테고리가 존재합니다."),
	STOCK_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "상품 재고가 없습니다."),
	DUPLICATED_PASSWORD(HttpStatus.CONFLICT, "기존 패스워드와 변경할 패스워드가 동일합니다."),
	NOT_CORRECT_PASSWORD(HttpStatus.FORBIDDEN, "기존 비밀번호와 일치하지 않습니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.");

	private final HttpStatus status;
	private final String message;

	public String format(Object... args) {
		return MessageFormat.format(this.message, args);
	}

}
