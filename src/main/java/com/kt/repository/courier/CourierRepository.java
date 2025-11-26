package com.kt.repository.courier;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kt.domain.entity.CourierEntity;

@Repository
public interface CourierRepository extends JpaRepository<CourierEntity, UUID>, CourierRepositoryCustom {
	Optional<CourierEntity> findByEmail(String email);
}
