package org.yenbo.springDemo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jndi.JndiTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.yenbo.springDemo.dao.UsersDao;
import org.yenbo.springDemo.response.User;

@RestController
@RequestMapping("/jndiDemo")
public class JndiDemoController {

	private static final Logger log = LoggerFactory.getLogger(JndiDemoController.class);
	
	@RequestMapping(method=RequestMethod.GET)
	public List<User> getMethod() {
	
		// Use the following URL for demo: "http://localhost:8080/springDemo/jndiDemo"
		try {
			// TODO refactor and use DI
			DataSource dataSource = (DataSource) new JndiTemplate().lookup("java:/comp/env/jdbc/demo");
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			UsersDao usersDao = new UsersDao(jdbcTemplate);
			return usersDao.getAll();
			
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			return new ArrayList<>();
		}
	}

}