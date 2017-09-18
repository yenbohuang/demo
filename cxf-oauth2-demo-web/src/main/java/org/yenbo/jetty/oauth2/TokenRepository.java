package org.yenbo.jetty.oauth2;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;
import org.apache.cxf.rs.security.oauth2.utils.OAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.jetty.exception.AccessTokenExistedException;
import org.yenbo.jetty.exception.RefreshTokenExistedException;

public class TokenRepository {

	private static final Logger log = LoggerFactory.getLogger(TokenRepository.class);
	
	private Map<String, ServerAccessToken> accessTokenMap = new HashMap<>();
	private Map<String, RefreshToken> refreshTokenMap = new HashMap<>(); 
	
	public void saveAccessToken(ServerAccessToken accessToken) {
		
		if (accessTokenMap.containsKey(accessToken.getTokenKey())) {
			AccessTokenExistedException exception = new AccessTokenExistedException(
					"Existing access token found");
			exception.addContextValue("tokenKey", accessToken.getTokenKey());
			throw exception;
		}
		
		accessTokenMap.put(accessToken.getTokenKey(), accessToken);
		
		log.debug("tokenKey={}, tokenType={}, expiresIn={}, refreshToken={}, scopes={}",
				accessToken.getTokenKey(),
				accessToken.getTokenType(),
				accessToken.getExpiresIn(),
				accessToken.getRefreshToken(),
				OAuthUtils.convertPermissionsToScopeList(accessToken.getScopes()));
	}
	
	public void saveRefreshToken(RefreshToken refreshToken) {
		
		if (refreshTokenMap.containsKey(refreshToken.getTokenKey())) {
			RefreshTokenExistedException exception = new RefreshTokenExistedException(
					"Existing refresh token found");
			exception.addContextValue("tokenKey", refreshToken.getTokenKey());
			throw exception;
		}
				
		refreshTokenMap.put(refreshToken.getTokenKey(), refreshToken);
		
		log.debug("tokenKey={}, tokenType={}, expiresIn={}, accessTokens={}, scopes={}",
				refreshToken.getTokenKey(),
				refreshToken.getTokenType(),
				refreshToken.getExpiresIn(),
				refreshToken.getAccessTokens(),
				OAuthUtils.convertPermissionsToScopeList(refreshToken.getScopes()));
	}
	
	public RefreshToken getRefreshToken(String refreshTokenKey) {
		
		RefreshToken refreshToken = refreshTokenMap.get(refreshTokenKey);
		
		if (null != refreshToken) {
			log.debug("Found: tokenKey={}, tokenType={}, expiresIn={}, accessTokens={}, scopes={}",
					refreshToken.getTokenKey(),
					refreshToken.getTokenType(),
					refreshToken.getExpiresIn(),
					refreshToken.getAccessTokens(),
					OAuthUtils.convertPermissionsToScopeList(refreshToken.getScopes()));
			
		} else {
			log.debug("Not found: {}", refreshTokenKey);
		}
		
		return refreshToken;
	}
	
	public void deleteRefreshToken(RefreshToken refreshToken) {
		
		if (refreshTokenMap.containsKey(refreshToken.getTokenKey())) {
			refreshTokenMap.remove(refreshToken.getTokenKey());
			log.debug("Deleted: tokenKey={}, tokenType={}, expiresIn={}, accessTokens={}, scopes={}",
					refreshToken.getTokenKey(),
					refreshToken.getTokenType(),
					refreshToken.getExpiresIn(),
					refreshToken.getAccessTokens(),
					OAuthUtils.convertPermissionsToScopeList(refreshToken.getScopes()));
		} else {
			log.warn("Not found: tokenKey={}, tokenType={}, expiresIn={}, accessTokens={}",
					refreshToken.getTokenKey(),
					refreshToken.getTokenType(),
					refreshToken.getExpiresIn(),
					refreshToken.getAccessTokens());
		}
	}
	
	public ServerAccessToken getAccessToken(String accessToken) {
		
		ServerAccessToken serverAccessToken = accessTokenMap.get(accessToken);
		
		if (null != serverAccessToken) {
			log.debug("Found: tokenKey={}, tokenType={}, expiresIn={}, refreshToken={}, scopes={}",
					serverAccessToken.getTokenKey(),
					serverAccessToken.getTokenType(),
					serverAccessToken.getExpiresIn(),
					serverAccessToken.getRefreshToken(),
					OAuthUtils.convertPermissionsToScopeList(serverAccessToken.getScopes()));
		} else {
			log.debug("Not found: {}", accessToken);
		}
		
		return serverAccessToken;
	}
	
	public void deleteAccessToken(ServerAccessToken accessToken) {
		
		if (accessTokenMap.containsKey(accessToken.getTokenKey())) {
			accessTokenMap.remove(accessToken.getTokenKey());
			log.debug("Deleted: tokenKey={}, tokenType={}, expiresIn={}, refreshToken={}, scopes={}",
					accessToken.getTokenKey(),
					accessToken.getTokenType(),
					accessToken.getExpiresIn(),
					accessToken.getRefreshToken(),
					OAuthUtils.convertPermissionsToScopeList(accessToken.getScopes()));
		} else {
			log.warn("Not found: tokenKey={}, tokenType={}, expiresIn={}, refreshToken={}",
					accessToken.getTokenKey(),
					accessToken.getTokenType(),
					accessToken.getExpiresIn(),
					accessToken.getRefreshToken());
		}
	}
}
