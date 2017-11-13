package org.yenbo.jetty.oauth2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.services.ClientRegistration;
import org.apache.cxf.rs.security.oauth2.services.ClientRegistrationResponse;
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
	
	public static final List<String> GRANT_TYPE_LIST = Arrays.<String>asList(
			OAuthConstants.AUTHORIZATION_CODE_GRANT,
			OAuthConstants.REFRESH_TOKEN);
	public static final List<String> RESPONSE_TYPE_LIST = Arrays.<String>asList(
			OAuthConstants.CODE_RESPONSE_TYPE,
			OAuthConstants.TOKEN_RESPONSE_TYPE);
	
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
	
	public static Client createClient() {
		
		Client client = new Client();
		
		client.setConfidential(true);
		client.setTokenEndpointAuthMethod(OAuthConstants.TOKEN_ENDPOINT_AUTH_POST);
		client.setAllowedGrantTypes(GRANT_TYPE_LIST);
		
		return client;
	}
	
	public static Client create(InMemoryClient inMemoryClient) {
		
		if (null == inMemoryClient) {
			throw new IllegalArgumentException("inMemoryClient is null.");
		}
		
		Client client = createClient();
		
		client.setClientId(inMemoryClient.getClientId().toString());
		client.setClientSecret(inMemoryClient.getClientSecret());
		client.getRedirectUris().addAll(inMemoryClient.getRedirectUris());
		client.getRegisteredScopes().addAll(inMemoryClient.getScopes());
		client.setApplicationDescription(inMemoryClient.getDescription());
		client.setApplicationName(inMemoryClient.getName());
		
		// client_name#<language tag>
		if (!inMemoryClient.getNameI18nMap().isEmpty()) {
			for (Entry<String, String> entry: inMemoryClient.getNameI18nMap().entrySet()) {
				client.getProperties().put(
						OAuthExtensionConstants.CLIENT_NAME_PREFIX + entry.getKey(),
						entry.getValue());
			}
		}
		    	
    	return client;
	}
	
	public static InMemoryClient create(Client client) {
		
		if (null == client) {
			throw new IllegalArgumentException("client is null.");
		}
		
		InMemoryClient inMemoryClient = new InMemoryClient();
		
		inMemoryClient.setClientId(UUID.fromString(client.getClientId()));
		inMemoryClient.setClientSecret(client.getClientSecret());
		inMemoryClient.getRedirectUris().addAll(client.getRedirectUris());
		inMemoryClient.getScopes().addAll(client.getRegisteredScopes());
		inMemoryClient.setDescription(client.getApplicationDescription());
		inMemoryClient.setName(client.getApplicationName());
		
		// client_name#<language tag>
		for (Entry<String, String> entry: client.getProperties().entrySet()) {
			if (entry.getKey().startsWith(OAuthExtensionConstants.CLIENT_NAME_PREFIX)) {
				Locale locale = Locale.forLanguageTag(
						entry.getKey().split(OAuthExtensionConstants.CLIENT_NAME_SPLITTER)[1]);
				inMemoryClient.getNameI18nMap().put(locale.toLanguageTag(), entry.getValue());
			}
		}		
		
		return inMemoryClient;
	}
	
	public static Client fill(Client client, ClientRegistration clientRegistration) {
		
		if (null == client) {
			throw new IllegalArgumentException("client is null.");
		}
		
		if (null == clientRegistration) {
			throw new IllegalArgumentException("clientRegistration is null.");
		}
		
		client.setApplicationDescription(clientRegistration.getStringProperty(
				OAuthExtensionConstants.CLIENT_DESCRIPTION));
		
		// client name L10N
		Map<String, Object> clientNameMap = clientRegistration.getMapProperty(
				OAuthExtensionConstants.CLIENT_NAME_I18N);
		if (null != clientNameMap && !clientNameMap.isEmpty()) {
			
			for (Entry<String, Object> entry: clientNameMap.entrySet()) {
				client.getProperties().put(entry.getKey(), (String) entry.getValue());
			}
		}
		
		return client;
	}
	
	public static ClientRegistrationResponse fill(
			ClientRegistrationResponse clientRegistrationResponse, Client client) {
		
		if (null == clientRegistrationResponse) {
			throw new IllegalArgumentException("clientRegistrationResponse is null.");
		}
		
		if (null == client) {
			throw new IllegalArgumentException("client is null.");
		}
		
		clientRegistrationResponse.setProperty(ClientRegistration.REDIRECT_URIS,
				client.getRedirectUris());
		clientRegistrationResponse.setProperty(ClientRegistration.CLIENT_NAME,
				client.getApplicationName());
		clientRegistrationResponse.setProperty(OAuthExtensionConstants.CLIENT_DESCRIPTION,
				client.getApplicationDescription());
		
		// There is a bug in "OAuthUtils.convertListOfScopesToString()" that it is not
		// space separated.
		clientRegistrationResponse.setProperty(ClientRegistration.SCOPE,
				convertListOfScopesToString(client.getRegisteredScopes()));
		
		clientRegistrationResponse.setProperty(ClientRegistration.TOKEN_ENDPOINT_AUTH_METHOD,
				client.getTokenEndpointAuthMethod());
		clientRegistrationResponse.setProperty(ClientRegistration.GRANT_TYPES,
				client.getAllowedGrantTypes());
		
		clientRegistrationResponse.setProperty(ClientRegistration.RESPONSE_TYPES,
				RESPONSE_TYPE_LIST);
		
		// client_name#<language tag>
		Map<String, String> clientNameMap = new HashMap<>();
		for (Entry<String, String> entry: client.getProperties().entrySet()) {
			if (entry.getKey().startsWith(OAuthExtensionConstants.CLIENT_NAME_PREFIX)) {
				clientNameMap.put(entry.getKey(), entry.getValue());
			}
		}
		clientRegistrationResponse.setProperty(OAuthExtensionConstants.CLIENT_NAME_I18N,
				clientNameMap);
		
		return clientRegistrationResponse;
	}
	
	public static BearerAccessToken create(Client client, InMemoryAccessToken inMemoryAccessToken) {
		
		if (null == client) {
			throw new IllegalArgumentException("client is null.");
		}
		
		if (null == inMemoryAccessToken) {
			throw new IllegalArgumentException("inMemoryAccessToken is null.");
		}
		
		BearerAccessToken accessToken = new BearerAccessToken(
				client,
				inMemoryAccessToken.getToken().toString(),
				inMemoryAccessToken.getExpiresIn(),
				inMemoryAccessToken.getIssueAt());
		
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
		
		RefreshToken refreshToken = new RefreshToken(
				client,
				inMemoryRefreshToken.getToken().toString(),
				inMemoryRefreshToken.getExpiresIn(),
				inMemoryRefreshToken.getIssueAt());
		
		refreshToken.setGrantCode(inMemoryRefreshToken.getAuthorizationCode());
		
		for (String scope: inMemoryRefreshToken.getScopes()) {
			refreshToken.getScopes().add(new OAuthPermission(scope));
		}
		
		if (null != inMemoryRefreshToken.getAccessToken()) {
			refreshToken.getAccessTokens().add(inMemoryRefreshToken.getAccessToken().toString());
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
	
	public static ClientRegistration createClientRegistration() {
		
		ClientRegistration clientRegistration = new ClientRegistration();
		
		clientRegistration.setGrantTypes(GRANT_TYPE_LIST);
		clientRegistration.setResponseTypes(RESPONSE_TYPE_LIST);
		clientRegistration.setTokenEndpointAuthMethod(OAuthConstants.TOKEN_ENDPOINT_AUTH_POST);
		
		return clientRegistration;
	}
	
	public static String convertListOfScopesToString(List<String> scopeList) {
		
		// There is a bug in "OAuthUtils.convertListOfScopesToString()" that it is not
		// space separated.
		String scopeString = "";
		
		if (null != scopeList && !scopeList.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			for (String scope: scopeList) {
				builder.append(scope).append(" ");
			}
			scopeString = builder.toString().trim();
		}
		
		return scopeString;
	}
}
