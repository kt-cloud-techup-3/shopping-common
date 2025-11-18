package com.kt.domain.constant.message;

import java.text.MessageFormat;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	INVALID_DOMAIN_FIELD(HttpStatus.BAD_REQUEST, "도메인 필드 오류 : {0}"),
	BODY_FIELD_ERROR(HttpStatus.BAD_REQUEST, "바디 필드 오류 : {0}"),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
	BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시판은 존재하지 않습니다."),
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글은 존재하지 않습니다."),
	REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글은 존재하지 않습니다.");

	private final HttpStatus status;
	private final String message;

	public String format(Object... args) {
		return MessageFormat.format(this.message, args);
	}

}
