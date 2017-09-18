package org.yenbo.jetty.oauth2;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.jetty.exception.AccessTokenExistedException;
import org.yenbo.jetty.exception.RefreshTokenExistedException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TokenRepository {

	private static final Logger log = LoggerFactory.getLogger(TokenRepository.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
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
		
		try {
			log.debug("{}", OBJECT_MAPPER.writeValueAsString(accessToken));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public void saveRefreshToken(RefreshToken refreshToken) {
		
		if (refreshTokenMap.containsKey(refreshToken.getTokenKey())) {
			RefreshTokenExistedException exception = new RefreshTokenExistedException(
					"Existing refresh token found");
			exception.addContextValue("tokenKey", refreshToken.getTokenKey());
			throw exception;
		}
				
		refreshTokenMap.put(refreshToken.getTokenKey(), refreshToken);
		
		try {
			log.debug("{}", OBJECT_MAPPER.writeValueAsString(refreshToken));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public RefreshToken getRefreshToken(String refreshTokenKey) {
		
		RefreshToken refreshToken = refreshTokenMap.get(refreshTokenKey);
		
		if (null != refreshToken) {
			try {
				log.debug("{}", OBJECT_MAPPER.writeValueAsString(refreshToken));
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
			}
			
		} else {
			log.debug("Not found: {}", refreshTokenKey);
		}
		
		return refreshToken;
	}
	
	public void deleteRefreshToken(RefreshToken refreshToken) {
		
		if (refreshTokenMap.containsKey(refreshToken.getTokenKey())) {
			refreshTokenMap.remove(refreshToken.getTokenKey());
			try {
				log.debug("{}", OBJECT_MAPPER.writeValueAsString(refreshToken));
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
			}
		} else {
			try {
				log.warn("Not found: {}", OBJECT_MAPPER.writeValueAsString(refreshToken));
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	public ServerAccessToken getAccessToken(String accessToken) {
		
		ServerAccessToken serverAccessToken = accessTokenMap.get(accessToken);
		
		if (null != serverAccessToken) {
			try {
				log.debug("{}", OBJECT_MAPPER.writeValueAsString(accessToken));
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
			}
		} else {
			log.debug("Not found: {}", accessToken);
		}
		
		return serverAccessToken;
	}
	
	public void deleteAccessToken(ServerAccessToken accessToken) {
		
		if (accessTokenMap.containsKey(accessToken.getTokenKey())) {
			accessTokenMap.remove(accessToken.getTokenKey());
			try {
				log.debug("{}", OBJECT_MAPPER.writeValueAsString(accessToken));
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
			}
		} else {
			try {
				log.warn("Not found: {}", OBJECT_MAPPER.writeValueAsString(accessToken));
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
}
