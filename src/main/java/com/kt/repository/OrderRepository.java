package com.kt.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kt.domain.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}
