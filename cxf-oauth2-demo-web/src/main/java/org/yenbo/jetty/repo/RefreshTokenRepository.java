package org.yenbo.jetty.repo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.jetty.data.InMemoryRefreshToken;
import org.yenbo.jetty.exception.RefreshTokenExistedException;

public class RefreshTokenRepository {

	private static final Logger log = LoggerFactory.getLogger(RefreshTokenRepository.class);
	
	private Map<String, InMemoryRefreshToken> refreshTokenMap = new HashMap<>();
	
	public void save(InMemoryRefreshToken refreshToken) {
		
		if (null == refreshToken) {
			throw new IllegalArgumentException("refreshToken is null.");
		}
		
		if (refreshTokenMap.containsKey(refreshToken.getToken())) {
			RefreshTokenExistedException exception = new RefreshTokenExistedException(
					"Existing refresh token found");
			exception.addContextValue("tokenKey", refreshToken.getToken());
			throw exception;
		}
				
		refreshTokenMap.put(refreshToken.getToken(), refreshToken);
		log.debug("Save: {}", refreshToken.getToken());
	}

	public InMemoryRefreshToken get(String refreshTokenKey) {
		
		if (StringUtils.isBlank(refreshTokenKey)) {
			throw new IllegalArgumentException("refreshTokenKey is blank.");
		}
		
		InMemoryRefreshToken refreshToken = refreshTokenMap.get(refreshTokenKey);
		
		if (null == refreshToken) {
			log.debug("Not found: {}", refreshTokenKey);
		} else {
			log.debug("Found: {}", refreshTokenKey);
		}
		
		return refreshToken;
	}
	
	public void delete(String refreshTokenKey) {
		
		if (StringUtils.isBlank(refreshTokenKey)) {
			throw new IllegalArgumentException("refreshTokenKey is blank.");
		}
		
		if (null == refreshTokenMap.remove(refreshTokenKey)) {
			log.warn("Not found: {}", refreshTokenKey);
		} else {
			log.debug("Delete: {}", refreshTokenKey);
		}
	}
}
