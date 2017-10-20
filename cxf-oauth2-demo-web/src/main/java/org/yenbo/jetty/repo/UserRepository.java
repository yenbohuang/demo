package org.yenbo.jetty.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.jetty.data.InMemoryUser;
import org.yenbo.jetty.data.PasswordInfo;

public class UserRepository {

	private static final Logger log = LoggerFactory.getLogger(UserRepository.class);
	
	private InMemoryUser user;
	
	public UserRepository() {
		user = new InMemoryUser();
		user.setPasswordInfo(new PasswordInfo("abcd", "1234", "password"));
		user.setUsername("yenbo");
		user.setProperty("property to be attached");
	}
	
	public InMemoryUser get(String username) {
		if (user.getUsername().equals(username)) {
			log.debug("Found: {}", username);
			return user;
		} else {
			log.debug("Not found: {}", username);
			return null;
		}
	}
}
