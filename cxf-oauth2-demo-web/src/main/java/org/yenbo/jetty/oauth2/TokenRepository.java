package org.yenbo.jetty.oauth2;

import java.util.HashSet;
import java.util.Set;

import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenRepository {

	private static final Logger log = LoggerFactory.getLogger(TokenRepository.class);
	
	private Set<ServerAccessToken> accessTokenSet = new HashSet<>();
	private Set<RefreshToken> refreshTokenSet = new HashSet<>();
	
	public void saveAccessToken(ServerAccessToken accessToken) {
		
		for (ServerAccessToken token: accessTokenSet) {
			if (token.getTokenKey().equals(accessToken.getTokenKey())) {
				log.debug("Existing access token found: " + token.getTokenKey());
				break;
			}
		}
		
		accessTokenSet.add(accessToken);
		
		log.debug("tokenKey={}, tokenType={}, expiresIn={}, refreshToken={}",
				accessToken.getTokenKey(),
				accessToken.getTokenType(),
				accessToken.getExpiresIn(),
				accessToken.getRefreshToken());
	}
	
	public void saveRefreshToken(RefreshToken refreshToken) {
		
		for (RefreshToken token: refreshTokenSet) {
			if (token.getTokenKey().equals(refreshToken.getTokenKey())) {
				log.debug("Existing refresh token found: " + token.getTokenKey());
				break;
			}
		}
		
		refreshTokenSet.add(refreshToken);
		
		log.debug("tokenKey={}, tokenType={}, expiresIn={}, accessTokens={}",
				refreshToken.getTokenKey(),
				refreshToken.getTokenType(),
				refreshToken.getExpiresIn(),
				refreshToken.getAccessTokens());
	}
}
