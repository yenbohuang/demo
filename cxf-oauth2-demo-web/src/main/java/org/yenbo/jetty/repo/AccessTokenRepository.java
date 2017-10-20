package org.yenbo.jetty.repo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.jetty.data.InMemoryAccessToken;
import org.yenbo.jetty.exception.AccessTokenExistedException;

public class AccessTokenRepository {

	private static final Logger log = LoggerFactory.getLogger(AccessTokenRepository.class);
	
	private Map<String, InMemoryAccessToken> accessTokenMap = new HashMap<>();
	
	public void save(InMemoryAccessToken accessToken) {
		
		if (null == accessToken) {
			throw new IllegalArgumentException("accessToken is null.");
		}
		
		if (accessTokenMap.containsKey(accessToken.getToken())) {
			AccessTokenExistedException exception = new AccessTokenExistedException(
					"Existing access token found");
			exception.addContextValue("tokenKey", accessToken.getToken());
			throw exception;
		}
		
		accessTokenMap.put(accessToken.getToken(), accessToken);
		log.debug("Save: {}", accessToken.getToken());
	}
	
	public InMemoryAccessToken get(String accessTokenKey) {
		
		if (StringUtils.isBlank(accessTokenKey)) {
			throw new IllegalArgumentException("accessTokenKey is blank.");
		}
		
		InMemoryAccessToken inMemoryAccessToken = accessTokenMap.get(accessTokenKey);
		
		if (null == inMemoryAccessToken) {
			log.debug("Not found: {}", accessTokenKey);
		} else {
			log.debug("Found: {}", accessTokenKey);
		}
		
		return inMemoryAccessToken;
	}
	
	public void delete(String accessTokenKey) {
		
		if (StringUtils.isBlank(accessTokenKey)) {
			throw new IllegalArgumentException("accessTokenKey is blank.");
		}
		
		if (null == accessTokenMap.remove(accessTokenKey)) {
			log.warn("Not found: {}", accessTokenKey);
		} else {
			log.debug("Delete: {}", accessTokenKey);
		}
	}
}
