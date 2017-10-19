package org.yenbo.jetty.oauth2.data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InMemoryRefreshToken {

	private UUID clientId;
	private String token;
	private long expiresIn;
	private long issueAt;
	private Set<String> scopes = new HashSet<>();
	private String authorizationCode;
	private String accessToken;
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public UUID getClientId() {
		return clientId;
	}
	
	public void setClientId(UUID clientId) {
		this.clientId = clientId;
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
	
	public Set<String> getScopes() {
		return scopes;
	}
	
	public String getAuthorizationCode() {
		return authorizationCode;
	}
	
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
