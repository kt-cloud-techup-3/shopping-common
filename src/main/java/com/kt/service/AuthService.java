package com.kt.service;

import com.kt.domain.dto.request.MemberRequest;


public interface AuthService {

	void memberSignup(MemberRequest.SignupMember request);

}
