package org.yenbo.jetty.data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InMemoryAuthorizationCode {

	private Set<String> scopes = new HashSet<>();
	private UUID clientId;
	private String code;
	private long expiresIn;
	private long issueAt;
	private String redirectUri;
	private String username;
	private String userProperty;
	
	public Set<String> getScopes() {
		return scopes;
	}
	
	public UUID getClientId() {
		return clientId;
	}
	
	public void setClientId(UUID clientId) {
		this.clientId = clientId;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public long getExpiresIn() {
		return expiresIn;
	}
	
	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}
	
	public long getIssueAt() {
		return issueAt;
	}
	
	public void setIssueAt(long issueAt) {
		this.issueAt = issueAt;
	}
	
	public String getRedirectUri() {
		return redirectUri;
	}
	
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUserProperty() {
		return userProperty;
	}
	
	public void setUserProperty(String userProperty) {
		this.userProperty = userProperty;
	}
}
