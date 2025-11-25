package com.kt.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kt.domain.entity.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
	List<OrderEntity> findAllByOrderBy_Id(UUID id);

	@Modifying
	@Query("UPDATE OrderEntity o SET o.orderBy = NULL WHERE o.orderBy.id = :userId")
	void clearUser(UUID userId);
}
