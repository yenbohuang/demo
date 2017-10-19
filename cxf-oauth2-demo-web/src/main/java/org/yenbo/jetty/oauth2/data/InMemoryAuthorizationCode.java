package org.yenbo.jetty.oauth2.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryAuthorizationCode {

	private List<String> approvedScopes = new ArrayList<>();
	private UUID clientId;
	private String code;
	private long expiresIn;
	private long issueAt;
	private String redirectUri;
	private String username;
	private String remoteAddress;
	
	public List<String> getApprovedScopes() {
		return approvedScopes;
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
	
	public String getRemoteAddress() {
		return remoteAddress;
	}
	
	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
}
