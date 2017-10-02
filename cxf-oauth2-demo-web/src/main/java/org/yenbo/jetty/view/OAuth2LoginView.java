package org.yenbo.jetty.view;

public class OAuth2LoginView {
	
	private boolean error;
	private String appName;
	
	public boolean isError() {
		return error;
	}
	
	public void setError(boolean error) {
		this.error = error;
	}
	
	public String getAppName() {
		return appName;
	}
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
}
