package com.kt.repository.courier;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kt.domain.entity.CourierEntity;

public interface CourierRepository extends JpaRepository<CourierEntity, UUID>, CourierRepositoryCustom {
}
