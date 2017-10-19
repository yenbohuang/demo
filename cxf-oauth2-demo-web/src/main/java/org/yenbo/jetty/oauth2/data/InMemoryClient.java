package org.yenbo.jetty.oauth2.data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InMemoryClient {

	private UUID clientId;
	private String clientSecret;
	private Set<String> redirectUris = new HashSet<>();
	private Set<String> scopes = new HashSet<>();
	private String description;
	private String name;
	
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
	
	public Set<String> getRedirectUris() {
		return redirectUris;
	}
	
	public Set<String> getScopes() {
		return scopes;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
