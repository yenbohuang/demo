package org.yenbo.jetty.security;

import java.io.Serializable;

public class InMemoryUser implements Serializable {

	private static final long serialVersionUID = -2596623224404349750L;
	
	private String username;
	private PasswordInfo passwordInfo;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public PasswordInfo getPasswordInfo() {
		return passwordInfo;
	}
	
	public void setPasswordInfo(PasswordInfo passwordInfo) {
		this.passwordInfo = passwordInfo;
	}
}
