package com.kt.repository;

import com.kt.domain.Gender;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.kt.domain.User;

import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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

		return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, loginId));
	}

	public void updatePassword(long id, String password) {
		// UPDATE {table} SET {column} = {value}, {column} = {value} WHERE {condition}
		var sql = "UPDATE MEMBER SET password = ? WHERE id = ?";

		jdbcTemplate.update(sql, password, id);
	}

	public boolean existsById(long id) {
		var sql = "SELECT EXISTS (SELECT id FROM MEMBER WHERE id = ?)";

		return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
	}

	public Optional<User> selectById(long id) {
		var sql = "SELECT * FROM MEMBER WHERE id = ?";
		// ResultSet객체로 반환을 함

		var list = jdbcTemplate.query(sql, rowMapper(), id);

		return list.stream().findFirst();
	}

	private RowMapper<User> rowMapper() {
		return (rs, rowNum) -> mapToUser(rs);
		// () -> {} 람다는 단일 실행문이면 {} 와 return 생략이 가능하다
	}

	private User mapToUser(ResultSet rs) throws SQLException {
		return new User(
			rs.getLong("id"),
			rs.getString("loginId"),
			rs.getString("password"),
			rs.getString("name"),
			rs.getString("email"),
			rs.getString("mobile"),
			Gender.valueOf(rs.getString("gender")),
			rs.getObject("birthday", LocalDate.class),
			rs.getObject("createdAt", LocalDateTime.class),
			rs.getObject("updatedAt", LocalDateTime.class)
		);
	}




}
