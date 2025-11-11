package com.kt.security;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class CurrentUser extends User {
	private Long id;

	public CurrentUser(String username, String password,
		Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}
}
