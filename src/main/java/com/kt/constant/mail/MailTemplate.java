package com.kt.constant.mail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MailTemplate {

	VERIFY_EMAIL("TECHUP-SHOPPING 이메일 주소 인증",
		"인증 코드를 입력하여 이메일 주소를 인증해 주세요.\n이 인증 코드는 발급 후 3분 동안만 유효합니다.");


	private final String subject;
	private final String notice;
}
