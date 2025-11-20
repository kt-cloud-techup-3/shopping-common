package com.kt.repository;

import com.kt.domain.entity.AbstractAccountEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AbstractAccountEntity, UUID> {

	Optional<AbstractAccountEntity> findByEmail(String email);
}
