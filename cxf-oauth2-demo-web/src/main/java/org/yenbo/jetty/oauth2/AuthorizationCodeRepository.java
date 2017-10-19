package org.yenbo.jetty.oauth2;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.jetty.oauth2.data.InMemoryAuthorizationCode;

public class AuthorizationCodeRepository {

	private static final Logger log = LoggerFactory.getLogger(AuthorizationCodeRepository.class);
	
	private Map<String, InMemoryAuthorizationCode> grantMap = new HashMap<>();
	
	public void save(InMemoryAuthorizationCode inMemoryAuthorizationCode) {
		
		if (null == inMemoryAuthorizationCode) {
			throw new IllegalArgumentException("inMemoryAuthorizationCode is null.");
		}
		
		grantMap.put(inMemoryAuthorizationCode.getCode(), inMemoryAuthorizationCode);
	}
	
	public InMemoryAuthorizationCode get(String authorizationCode) {
		
		if (StringUtils.isBlank(authorizationCode)) {
			throw new IllegalArgumentException("authorizationCode is blank");
		}
		
		InMemoryAuthorizationCode inMemoryAuthorizationCode = grantMap.get(authorizationCode);
		
		if (null == inMemoryAuthorizationCode) {
			log.debug("Not found: {}", authorizationCode);
		}
		
		return inMemoryAuthorizationCode;
	}
	
	public void delete(String code) {
		
		if (null == grantMap.remove(code)) {
			log.debug("Not found: {}", code);
		}
	}
}
