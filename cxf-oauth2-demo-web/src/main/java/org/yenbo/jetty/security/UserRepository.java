package org.yenbo.jetty.security;

public class UserRepository {

	private InMemoryUser user;
	
	public UserRepository() {
		user = new InMemoryUser();
		user.setPasswordInfo(new PasswordInfo("abcd", "1234", "password"));
		user.setUsername("yenbo");
		user.setProperty("property to be attached");
	}
	
	public InMemoryUser get(String username) {
		if (user.getUsername().equals(username)) {
			return user;
		} else {
			return null;
		}
	}
}
