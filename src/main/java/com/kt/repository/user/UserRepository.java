package com.kt.repository.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kt.constant.message.ErrorCode;
import com.kt.domain.entity.UserEntity;
import com.kt.exception.BaseException;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID>, UserRepositoryCustom {
	Optional<UserEntity> findByEmail(String email);

	default UserEntity findByIdOrThrow(UUID id) {
		return findById(id).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
	}
}
