package com.kt.domain.entity;

import com.kt.domain.constant.Gender;
import com.kt.domain.constant.UserRole;
import com.kt.domain.entity.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Getter
@Entity(name = "account")
@NoArgsConstructor(access = PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "account_type")
public abstract class AbstractAccountEntity extends BaseEntity {

	@Column(nullable = false)
	protected String name;

	@Column(nullable = false)
	protected String email;

	@Column(nullable = false)
	protected String password;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	protected UserRole role;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	protected Gender gender;


}
