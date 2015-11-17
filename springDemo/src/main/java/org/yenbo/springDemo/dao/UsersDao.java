package org.yenbo.springDemo.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.yenbo.springDemo.response.User;
import org.yenbo.springDemo.rowMapper.UserRowMapper;

public class UsersDao {

	private JdbcTemplate jdbcTemplate;
	
	public UsersDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<User> getAll() {
		return jdbcTemplate.query("select * from users", new UserRowMapper());
	}
}
