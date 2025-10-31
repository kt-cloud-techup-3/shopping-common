package com.kt.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kt.domain.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository {
	private final JdbcTemplate jdbcTemplate;

	public void save(User user) {
		// 서비스에서 dto를 도메인(비지니스모델)으로 바꾼다음 전달
		var sql = "INSERT INTO MEMBER (id, loginId, password, name, email, mobile, gender, birthday, createdAt, updatedAt) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.update(
			sql,
			user.getId(),
			user.getLoginId(),
			user.getPassword(),
			user.getName(),
			user.getEmail(),
			user.getMobile(),
			user.getGender().toString(),
			user.getBirthday().toString(),
			user.getCreatedAt().toString(),
			user.getUpdatedAt().toString()
		);
	}

	public Long findMaxId() {
		var sql = "SELECT MAX(id) FROM MEMBER";
		Long maxId = jdbcTemplate.queryForObject(sql, Long.class);
		return maxId;
	}
}
