package com.kt.repository.user;

import com.kt.common.CustomException;
import com.kt.common.ErrorCode;
import com.kt.domain.user.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Boolean existsByLoginId(String loginId);

	@Query("""
		SELECT exists (SELECT u FROM User u WHERE u.loginId = ?1)
		""")
	Boolean existsByLoginIdJPQL(String loginId);

	Page<User> findAllByNameContaining(String keyword, Pageable pageable);

	default User findByIdOrThrow(Long id, ErrorCode errorCode) {
		return findById(id).orElseThrow(
			() -> new CustomException(errorCode)
		);
	}
}
