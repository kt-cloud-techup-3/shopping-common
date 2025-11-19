package com.kt.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kt.domain.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
}
