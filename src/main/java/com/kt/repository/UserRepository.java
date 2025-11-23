package com.kt.repository;

import com.kt.constant.message.ErrorCode;
import com.kt.domain.entity.UserEntity;
import com.kt.exception.NotFoundException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
	default UserEntity findByUserIdOrThrow(UUID userId) {
		return findById(userId).orElseThrow( ()-> new NotFoundException(ErrorCode.USER_NOT_FOUND));
	}
	Optional<UserEntity> findByEmail(String email);
}
