package com.kt.repository.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kt.domain.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID>, UserRepositoryCustom {
	Optional<UserEntity> findByEmail(String email);
}
