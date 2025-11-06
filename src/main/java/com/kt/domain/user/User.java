package com.kt.domain.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.kt.common.BaseEntity;

import com.kt.domain.order.Order;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends BaseEntity {
	
	private String loginId;
	private String password;
	private String name;
	private String email;
	private String mobile;

	@Enumerated(EnumType.STRING)
	private Gender gender;
	private LocalDate birthday;

	@OneToMany(mappedBy = "user")
	private List<Order> orders = new ArrayList<>();

	public User(String loginId,
							String password,
							String name,
							String email,
							String mobile,
							Gender gender,
							LocalDate birthday,
							LocalDateTime createdAt,
							LocalDateTime updatedAt) {
		this.loginId = loginId;
		this.password = password;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.gender = gender;
		this.birthday = birthday;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}

	public void update(String name, String email, String mobile) {
		this.name = name;
		this.email = email;
		this.mobile = mobile;
	}



}
