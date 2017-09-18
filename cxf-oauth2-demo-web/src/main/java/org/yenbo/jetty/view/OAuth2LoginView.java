package org.yenbo.jetty.view;

public class OAuth2LoginView {
	
	private boolean error;
	private boolean logout;
	
	public boolean isError() {
		return error;
	}
	
	public void setError(boolean error) {
		this.error = error;
	}
	
	public boolean isLogout() {
		return logout;
	}
	
	public void setLogout(boolean logout) {
		this.logout = logout;
	}
}
