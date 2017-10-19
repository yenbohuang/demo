package org.yenbo.jetty.oauth2.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryAccessToken {

	private UUID clientId;
	private String token;
	private long expiresIn;
	private long issueAt;
	private List<String> scopes = new ArrayList<>();
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
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
	
	public UUID getClientId() {
		return clientId;
	}
	
	public void setClientId(UUID clientId) {
		this.clientId = clientId;
	}
	
	public List<String> getScopes() {
		return scopes;
	}
}
