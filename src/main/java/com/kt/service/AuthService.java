package com.kt.service;

import com.kt.domain.dto.request.LoginRequest;
import com.kt.domain.dto.request.MemberRequest;
import com.mysema.commons.lang.Pair;

public interface AuthService {

	void memberSignup(MemberRequest.SignupMember request);

	Pair<String, String> login(LoginRequest request);

}
