package org.yenbo.jetty.oauth2;

import java.util.Arrays;
import java.util.UUID;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.yenbo.jetty.data.InMemoryAccessToken;
import org.yenbo.jetty.data.InMemoryAuthorizationCode;
import org.yenbo.jetty.data.InMemoryClient;
import org.yenbo.jetty.data.InMemoryRefreshToken;

public class Oauth2Factory {

	public static final long ACCESS_TOKEN_EXPIRED_TIME_SECONDS = 12345L;
	public static final long REFRESH_TOKEN_EXPIRED_TIME_SECONDS = 67890L;
	
	public static final String KEY_USER_PROPERTY = "USER_PROPERTY";
	
	private Oauth2Factory() {
	}

	public static InMemoryAuthorizationCode create(ServerAuthorizationCodeGrant grant) {
		
		if (null == grant) {
			throw new IllegalArgumentException("grant is null");
		}
		
		InMemoryAuthorizationCode authorizationCode = new InMemoryAuthorizationCode();
		authorizationCode.getScopes().addAll(grant.getApprovedScopes());
		authorizationCode.setClientId(UUID.fromString(grant.getClient().getClientId()));
		authorizationCode.setCode(grant.getCode());
		authorizationCode.setExpiresIn(grant.getExpiresIn());
		authorizationCode.setIssueAt(grant.getIssuedAt());
		authorizationCode.setRedirectUri(grant.getRedirectUri());
		
		// UserSubject
		authorizationCode.setUsername(grant.getSubject().getLogin());
		authorizationCode.setUserProperty(grant.getSubject().getProperties().get(KEY_USER_PROPERTY));
		
		return authorizationCode;
	}
	
	public static ServerAuthorizationCodeGrant create(Client client,
			InMemoryAuthorizationCode inMemoryAuthorizationCode) {
		
		if (null == inMemoryAuthorizationCode) {
			throw new IllegalArgumentException("inMemoryAuthorizationCode is null.");
		}
		
		if (null == client) {
			throw new IllegalArgumentException("client is null.");
		}
		
		ServerAuthorizationCodeGrant grant = new ServerAuthorizationCodeGrant();
		grant.getApprovedScopes().addAll(inMemoryAuthorizationCode.getScopes());
		grant.setClient(client);
		grant.setCode(inMemoryAuthorizationCode.getCode());
		grant.setExpiresIn(inMemoryAuthorizationCode.getExpiresIn());
		grant.setIssuedAt(inMemoryAuthorizationCode.getIssueAt());
		grant.setRedirectUri(inMemoryAuthorizationCode.getRedirectUri());
		
		// UserSubject
		UserSubject subject = new UserSubject();
		subject.setLogin(inMemoryAuthorizationCode.getUsername());
		subject.getProperties().put(KEY_USER_PROPERTY, inMemoryAuthorizationCode.getUserProperty());
		grant.setSubject(subject);
		
		return grant;
	}
	
	public static Client create(InMemoryClient inMemoryClient) {
		
		if (null == inMemoryClient) {
			throw new IllegalArgumentException("inMemoryClient is null.");
		}
		
		Client client = new Client();
		
		// set default values
		client.setConfidential(true);
		client.setTokenEndpointAuthMethod(OAuthConstants.TOKEN_ENDPOINT_AUTH_POST);
		client.setAllowedGrantTypes(Arrays.<String>asList(
				OAuthConstants.AUTHORIZATION_CODE_GRANT,
				OAuthConstants.REFRESH_TOKEN
				));
		
		// set customized fields
		client.setClientId(inMemoryClient.getClientId().toString());
		client.setClientSecret(inMemoryClient.getClientSecret());
		client.getRedirectUris().addAll(inMemoryClient.getRedirectUris());
		client.getRegisteredScopes().addAll(inMemoryClient.getScopes());
		client.setApplicationDescription(inMemoryClient.getDescription());
		client.setApplicationName(inMemoryClient.getName());
		    	
    	return client;
	}
	
	public static BearerAccessToken create(Client client, InMemoryAccessToken inMemoryAccessToken) {
		
		if (null == client) {
			throw new IllegalArgumentException("client is null.");
		}
		
		if (null == inMemoryAccessToken) {
			throw new IllegalArgumentException("inMemoryAccessToken is null.");
		}
		
		BearerAccessToken accessToken = new BearerAccessToken(client, inMemoryAccessToken.getToken(),
				inMemoryAccessToken.getExpiresIn(), inMemoryAccessToken.getIssueAt());
		
		accessToken.setGrantCode(inMemoryAccessToken.getAuthorizationCode());
		
		for (String scope: inMemoryAccessToken.getScopes()) {
			accessToken.getScopes().add(new OAuthPermission(scope));
		}
		
		// UserSubject
		UserSubject subject = new UserSubject();
		subject.setLogin(inMemoryAccessToken.getUsername());
		subject.getProperties().put(KEY_USER_PROPERTY, inMemoryAccessToken.getUserProperty());
		accessToken.setSubject(subject);
		
		return accessToken;
	}
	
	public static InMemoryAccessToken create(ServerAccessToken serverAccessToken) {
		
		if (null == serverAccessToken) {
			throw new IllegalArgumentException("serverAccessToken is null.");
		}
		
		InMemoryAccessToken inMemoryAccessToken = new InMemoryAccessToken();
		
		inMemoryAccessToken.setAuthorizationCode(serverAccessToken.getGrantCode());
		inMemoryAccessToken.setClientId(UUID.fromString(serverAccessToken.getClient().getClientId()));
		inMemoryAccessToken.setExpiresIn(serverAccessToken.getExpiresIn());
		inMemoryAccessToken.setIssueAt(serverAccessToken.getIssuedAt());
		
		for (OAuthPermission permission: serverAccessToken.getScopes()) {
			inMemoryAccessToken.getScopes().add(permission.getPermission());
		}
		
		inMemoryAccessToken.setToken(serverAccessToken.getTokenKey());
		
		// UserSubject
		inMemoryAccessToken.setUsername(serverAccessToken.getSubject().getLogin());
		inMemoryAccessToken.setUserProperty(
				serverAccessToken.getSubject().getProperties().get(KEY_USER_PROPERTY));
		
		return inMemoryAccessToken;
	}
	
	public static RefreshToken create(Client client, InMemoryRefreshToken inMemoryRefreshToken) {
		
		if (null == inMemoryRefreshToken) {
			throw new IllegalArgumentException("inMemoryRefreshToken is null.");
		}
		
		if (null == client) {
			throw new IllegalArgumentException("client is null.");
		}
		
		RefreshToken refreshToken = new RefreshToken(client,
				inMemoryRefreshToken.getToken(),
				inMemoryRefreshToken.getExpiresIn(),
				inMemoryRefreshToken.getIssueAt());
		
		refreshToken.setGrantCode(inMemoryRefreshToken.getAuthorizationCode());
		
		for (String scope: inMemoryRefreshToken.getScopes()) {
			refreshToken.getScopes().add(new OAuthPermission(scope));
		}
		
		if (null != inMemoryRefreshToken.getAccessToken()) {
			refreshToken.getAccessTokens().add(inMemoryRefreshToken.getAccessToken());
		}
		
		// UserSubject
		UserSubject subject = new UserSubject();
		subject.setLogin(inMemoryRefreshToken.getUsername());
		subject.getProperties().put(KEY_USER_PROPERTY, inMemoryRefreshToken.getUserProperty());
		refreshToken.setSubject(subject);
		
		return refreshToken;
	}
	
	public static InMemoryRefreshToken create(RefreshToken refreshToken) {
		
		if (null == refreshToken) {
			throw new IllegalArgumentException("refreshToken is null.");
		}
		
		InMemoryRefreshToken inMemoryRefreshToken = new InMemoryRefreshToken();
		
		if (!refreshToken.getAccessTokens().isEmpty()) {
			inMemoryRefreshToken.setAccessToken(refreshToken.getAccessTokens().get(0));
		}
		
		inMemoryRefreshToken.setClientId(UUID.fromString(refreshToken.getClient().getClientId()));
		inMemoryRefreshToken.setExpiresIn(refreshToken.getExpiresIn());
		inMemoryRefreshToken.setAuthorizationCode(refreshToken.getGrantCode());
		inMemoryRefreshToken.setIssueAt(refreshToken.getIssuedAt());
		
		for (OAuthPermission permission: refreshToken.getScopes()) {
			inMemoryRefreshToken.getScopes().add(permission.getPermission());
		}
		
		inMemoryRefreshToken.setToken(refreshToken.getTokenKey());
		
		// UserSubject
		inMemoryRefreshToken.setUsername(refreshToken.getSubject().getLogin());
		inMemoryRefreshToken.setUserProperty(
				refreshToken.getSubject().getProperties().get(KEY_USER_PROPERTY));
		
		return inMemoryRefreshToken;
	}
}
