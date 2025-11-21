package com.kt.service;

import com.kt.domain.dto.request.LoginRequest;
import com.kt.domain.dto.request.SignupRequest;
import com.mysema.commons.lang.Pair;

public interface AuthService {

	void memberSignup(SignupRequest.SignupMember request);

	Pair<String, String> login(LoginRequest request);

	void sendAuthCode(SignupRequest.SignupEmail request);
}
