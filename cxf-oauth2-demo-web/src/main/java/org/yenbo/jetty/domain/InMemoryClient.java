package org.yenbo.jetty.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class InMemoryClient {

	private UUID clientId;
	private String clientSecret;
	private String redirectUri;
	private List<InMemoryAuthorizationCode> authorizationCodeList = new ArrayList<>();
	
	public UUID getClientId() {
		return clientId;
	}
	
	public void setClientId(UUID clientId) {
		this.clientId = clientId;
	}
	
	public String getClientSecret() {
		return clientSecret;
	}
	
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
	public String getRedirectUri() {
		return redirectUri;
	}
	
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	
	public List<InMemoryAuthorizationCode> getAuthorizationCodeList() {
		return authorizationCodeList;
	}
}
