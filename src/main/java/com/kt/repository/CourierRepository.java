package com.kt.repository;

import com.kt.domain.entity.CourierEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourierRepository extends JpaRepository<CourierEntity, UUID> {

	Optional<CourierEntity> findByEmail(String email);
}
