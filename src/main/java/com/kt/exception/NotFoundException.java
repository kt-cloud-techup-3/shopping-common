package com.kt.exception;

import com.kt.constant.message.ErrorCode;

public class NotFoundException extends BaseException {

	public NotFoundException(ErrorCode error) {
		super(error);
	}
}
