package org.yenbo.jetty.oauth2;

import java.util.HashSet;
import java.util.Set;

import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorizationCodeRepository {

	private static final Logger log = LoggerFactory.getLogger(AuthorizationCodeRepository.class);
	
	private Set<ServerAuthorizationCodeGrant> grantSet = new HashSet<>();
	
	public void saveAuthorizationCode(ServerAuthorizationCodeGrant grant) {
		grantSet.add(grant);
		log.debug("Save authorization code: code={}", grant.getCode());
	}
	
	public ServerAuthorizationCodeGrant getAuthorizationCode(String authorizationCode) {
		
		for (ServerAuthorizationCodeGrant grant: grantSet) {
			if (grant.getCode().equals(authorizationCode)) {
				log.debug("Get authorization code: {}", authorizationCode);
				return grant;
			}
		}
		
		return null;
	}
	
	public void deleteAuthorizationCode(ServerAuthorizationCodeGrant grant) {
		if (grantSet.remove(grant)) {
			log.debug("Authorization code deleted: {}", grant.getCode());
		} else {
			log.debug("Authorization code not found when deleting it: {}", grant.getCode());
		}
	}
}
