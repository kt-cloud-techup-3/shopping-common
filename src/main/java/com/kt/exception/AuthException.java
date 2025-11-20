package com.kt.exception;

import com.kt.constant.message.ErrorCode;

public class AuthException extends BaseException {

	public AuthException(ErrorCode error) {
		super(error);
	}
}
