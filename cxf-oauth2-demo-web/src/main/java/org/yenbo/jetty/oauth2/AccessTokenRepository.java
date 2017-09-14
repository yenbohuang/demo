package org.yenbo.jetty.oauth2;

import java.util.HashSet;
import java.util.Set;

import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessTokenRepository {

	private static final Logger log = LoggerFactory.getLogger(AccessTokenRepository.class);
	
	private Set<ServerAccessToken> accessTokenSet = new HashSet<>();
	
	public void saveAccessToken(ServerAccessToken accessToken) {
		accessTokenSet.add(accessToken);
		log.debug("Save accessToken: refreshToken={}, responseType={}, tokenKey={}, tokenType={}",
				accessToken.getRefreshToken(), accessToken.getResponseType(),
				accessToken.getTokenKey(), accessToken.getTokenType());
	}
}
