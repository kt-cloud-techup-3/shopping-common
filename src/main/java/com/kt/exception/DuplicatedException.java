package com.kt.exception;

import com.kt.constant.message.ErrorCode;

public class DuplicatedException extends BaseException{

	public DuplicatedException(ErrorCode error) {
		super(error);
	}
}
