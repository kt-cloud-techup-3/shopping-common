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

		var sql = """
			INSERT 
				INTO 
			MEMBER (
					id, 
					loginId, 
					password, 
					name, 
					birthday,
					mobile,
					email,
					gender,
					createdAt,
					updatedAt
				) 
			VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
			""";

		jdbcTemplate.update(
			sql,
			user.getId(),
			user.getLoginId(),
			user.getPassword(),
			user.getName(),
			user.getBirthday(),
			user.getMobile(),
			user.getEmail(),
			user.getGender().name(),
			user.getCreatedAt(),
			user.getUpdatedAt()
		);

	}

	public Long selectMaxId() {
		var sql = "SELECT MAX(id) FROM MEMBER";

		var maxId = jdbcTemplate.queryForObject(sql, Long.class);
		return maxId == null ? 0L : maxId;
	}

	// count 하는것은 데이터를 모두 훑어야 하기에(full-scan) 비추
	// exists로 존재 여부 체크 -> boolean으로 값 존재 여부를 바로 알 수 있음
	public boolean existsByLoginId(String loginId) {
		var sql = "SELECT EXISTS (SELECT id FROM MEMBER WHERE loginId = ?)";

		return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class));
	}


}
