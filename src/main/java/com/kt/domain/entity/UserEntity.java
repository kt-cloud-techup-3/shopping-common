package com.kt.domain.entity;

import com.kt.domain.constant.Gender;
import com.kt.domain.constant.UserRole;
import com.kt.domain.constant.UserStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static lombok.AccessLevel.*;

@Getter
@Entity(name = "\"user\"")
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorValue("USER")
public class UserEntity extends AbstractAccountEntity {

	@Column(nullable = false)
	private LocalDate birth;

	@Column(nullable = false)
	private String mobile;

	@OneToMany(
		mappedBy = "createdBy",
		cascade = CascadeType.REMOVE,
		orphanRemoval = true
	)
	List<AddressEntity> addresses;


	protected UserEntity(
		String name,
		String email,
		String password,
		UserRole role,
		Gender gender,
		LocalDate birth,
		String mobile
	) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.gender = gender;
		this.birth = birth;
		this.mobile = mobile;
		this.status = UserStatus.ENABLED;
	}

	public static UserEntity create(
		final String name,
		final String email,
		final String password,
		final UserRole role,
		final Gender gender,
		final LocalDate birth,
		final String mobile
	) {
		return new UserEntity(
			name, email, password, role, gender, birth, mobile
		);
	}

}
